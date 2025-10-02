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
import com.example.pocketlibrary.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
        val author = etAuthor.text.toString().trim()
        val year = etYear.text.toString().trim()

        if (title.isEmpty() || author.isEmpty() || year.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val id = UUID.randomUUID().toString()
        val firestore = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()

        if (photoUri != null) {
            val ref = storage.reference.child("covers/$id.jpg")
            ref.putFile(photoUri!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { url ->
                        val book = mapOf(
                            "id" to id,
                            "title" to title,
                            "author" to author,
                            "year" to year,
                            "coverUrl" to url.toString()
                        )
                        firestore.collection("books").document(id).set(book)
                    }
                }
        } else {
            val book = mapOf(
                "id" to id,
                "title" to title,
                "author" to author,
                "year" to year,
                "coverUrl" to null
            )
            firestore.collection("books").document(id).set(book)
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
