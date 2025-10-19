package com.example.pocketlibrary.create

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pocketlibrary.Book
import com.example.pocketlibrary.R
import com.example.pocketlibrary.internalDatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import com.example.pocketlibrary.internalDatabase.SyncManager
import com.example.pocketlibrary.Shelf

class CreateFragment : Fragment() {

    private lateinit var etTitle: EditText
    private lateinit var etAuthor: EditText
    private lateinit var etYear: EditText
    private lateinit var imgCover: ImageView
    private lateinit var btnCamera: Button
    private lateinit var btnSave: Button

    private var photoUri: Uri? = null

    // Camera
    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && photoUri != null) {
            imgCover.setImageURI(photoUri)
        } else {
            Toast.makeText(requireContext(), "No photo captured", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_create, container, false)

        etTitle = view.findViewById(R.id.etTitle)
        etAuthor = view.findViewById(R.id.etAuthor)
        etYear = view.findViewById(R.id.etYear)
        imgCover = view.findViewById(R.id.imgCover)
        btnCamera = view.findViewById(R.id.btnCamera)
        btnSave = view.findViewById(R.id.btnSave)

        btnCamera.setOnClickListener {
            requestCameraPermission.launch(android.Manifest.permission.CAMERA)
        }
        btnSave.setOnClickListener { saveBook() }

        return view
    }

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) openRealCamera()
            else Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
        }

    private fun openRealCamera() {
        val context = requireContext()
        val imageFile = File(context.filesDir, "images")
        if (!imageFile.exists()) {
            imageFile.mkdirs()
        }

        val fileName = "cover_${System.currentTimeMillis()}.jpg"
        val photoFile = File(imageFile, fileName)
        photoUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile
        )
        takePhotoLauncher.launch(photoUri)
    }

    private fun saveBook() {
        val title = etTitle.text.toString().trim()
        val authorInput = etAuthor.text.toString().trim()
        val yearInput = etYear.text.toString().trim()

        if (title.isEmpty() || authorInput.isEmpty() || yearInput.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val key = UUID.randomUUID().toString()
        val authorList = listOf(authorInput)
        val publishYear = yearInput.toIntOrNull()

        val book = Book(
            key = key,
            title = title,
            author = authorList,
            coverId = R.drawable.ic_placeholder,
            publishYear = publishYear,
            isFavourite = false,
            coverUri = photoUri?.toString()
        )

        val db = AppDatabase.getDatabase(requireContext())


        lifecycleScope.launch(Dispatchers.IO) {


            db.bookDao().insert(book)
            val shelves = db.shelfDAO().getAllShelves()
            val localExists = shelves.any { it.shelfName == "local" }
            if (!localExists) {
                db.shelfDAO().insertShelf(Shelf("local", emptyList()))
            }
            db.shelfDAO().addBookIdToShelf("local",book.key)

            try {
                SyncManager.addBookToFirebase(book)
                if (!localExists) {
                    SyncManager.addShelfToFirebase(Shelf("local", emptyList()))
                }
                SyncManager.addBookIdToShelf("local", book.key)
            }catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Book saved locally. Will sync when online.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        Toast.makeText(requireContext(), "Book added!", Toast.LENGTH_SHORT).show()
        clearForm()
    }



    private fun clearForm() {
        etTitle.text.clear()
        etAuthor.text.clear()
        etYear.text.clear()
        imgCover.setImageResource(R.drawable.ic_placeholder)
        photoUri = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("photoUri", photoUri?.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val savedUri = savedInstanceState?.getString("photoUri")
        if (savedUri != null) {
            photoUri = Uri.parse(savedUri)
            imgCover.setImageURI(photoUri)
        }
    }
}

