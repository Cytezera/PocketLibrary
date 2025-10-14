package com.example.pocketlibrary.create

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pocketlibrary.Book
import com.example.pocketlibrary.R
import com.example.pocketlibrary.internalDatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

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
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            imgCover.setImageBitmap(bitmap)
            photoUri = saveImageToGallery(requireContext(), bitmap)
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

        btnCamera.setOnClickListener { takePhotoLauncher.launch(null) }
        btnSave.setOnClickListener { saveBook() }

        return view
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
        val coverId = if (photoUri != null) {
            R.drawable.ic_placeholder
        } else 0

        val book = Book(
            key = key,
            title = title,
            author = authorList,
            coverId = coverId,
            publishYear = publishYear
        )

        val db = AppDatabase.getDatabase(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            db.bookDao().insert(book)
        }

        Toast.makeText(requireContext(), "Book added!", Toast.LENGTH_SHORT).show()
        clearForm()
    }

    private fun saveImageToGallery(context: Context, bitmap: Bitmap): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "cover_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            resolver.openOutputStream(it)?.use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
        }
        return uri
    }

    private fun clearForm() {
        etTitle.text.clear()
        etAuthor.text.clear()
        etYear.text.clear()
        imgCover.setImageResource(R.drawable.ic_placeholder)
        photoUri = null
    }
}
