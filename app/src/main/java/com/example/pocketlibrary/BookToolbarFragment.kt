package com.example.pocketlibrary


import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.pocketlibrary.internalDatabase.BookDAO
import com.example.pocketlibrary.internalDatabase.AppDatabase
import kotlinx.coroutines.launch
import com.example.pocketlibrary.internalDatabase.SyncManager




class BookToolbarFragment : Fragment() {

    private var book: Book? = null

    private lateinit var db: AppDatabase

    // private lateinit var firebaseSyncManager: FirebaseSyncManager
    private lateinit var bookDao: BookDAO
    companion object {
        private const val ARG_BOOK = "arg_book"

        fun newInstance(book: Book): BookToolbarFragment {
            val fragment = BookToolbarFragment()
            val args = Bundle()
            args.putParcelable(ARG_BOOK, book)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        book = arguments?.getParcelable<Book>(ARG_BOOK)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_toolbar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        val btnSave = view.findViewById<ImageView>(R.id.btnSave)
        val btnShare = view.findViewById<ImageView>(R.id.btnShare)
        val btnAddToShelf = view.findViewById<ImageView>(R.id.btnAddToShelf)

        db = AppDatabase.getDatabase(requireContext())
        bookDao = db.bookDao()
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        book?.let { currentBook ->
            lifecycleScope.launch {

                val tempBook = bookDao.getBookByKey(currentBook.key)
                val isFavourite = tempBook?.isFavourite == false

                if (currentBook.isFavourite) {
                    btnSave.setImageResource(R.drawable.ic_saved) // change to saved icon
                } else {
                    btnSave.setImageResource(R.drawable.ic_save) // default save icon
                }
            }
        }

//        btnAddToShelf.setOnClickListener {
//            book?.let { currentBook ->
//                lifecycleScope.launch {
//                    // Get all existing shelves from Room
//                    val shelves = db.shelfDAO().getAllShelves()
//                    val shelfNames = shelves.map { it.shelfName }.toMutableList()
//                    val options = arrayOf("Create New Shelf") + shelfNames
//                    val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
//                    builder.setTitle("Add to Shelf")
//                    builder.setItems(options) { dialog, which ->
//                        if (which == 0) {
//                            // Create new shelf
//                            val input = android.widget.EditText(requireContext())
//                            input.hint = "Enter shelf name"
//
//                            androidx.appcompat.app.AlertDialog.Builder(requireContext())
//                                .setTitle("New Shelf")
//                                .setView(input)
//                                .setPositiveButton("Create") { innerDialog, _ ->
//                                    val shelfName = input.text.toString().trim()
//                                    if (shelfName.isNotEmpty()) {
//                                        addBookToShelf(currentBook, shelfName)
//                                    }
//                                    innerDialog.dismiss()
//                                }
//                                .setNegativeButton("Cancel") { innerDialog, _ -> innerDialog.cancel() }
//                                .show()
//                        } else {
//                            val selectedShelf = options[which]
//                            addBookToShelf(currentBook, selectedShelf)
//                        }
//                    }
//                    builder.show()
//                }
//            }
//        }
        btnAddToShelf.setOnClickListener {
            book?.let { currentBook ->
                lifecycleScope.launch {
                    val shelves = db.shelfDAO().getAllShelves()
                    val shelfNames = shelves.map { it.shelfName }

                    // Check which shelves already contain this book
                    val checkedItems = shelfNames.map { shelf ->
                        val shelfData = db.shelfDAO().getShelfByName(shelf)
                        shelfData?.bookIds?.contains(currentBook.key) == true
                    }.toBooleanArray()

                    val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    builder.setTitle("Add to Shelves")

                    builder.setMultiChoiceItems(
                        shelfNames.toTypedArray(),
                        checkedItems
                    ) { _, which, isChecked ->
                        checkedItems[which] = isChecked
                    }

                    builder.setPositiveButton("Save") { _, _ ->
                        lifecycleScope.launch {
                            var addedToShelf = false
                            for (i in shelfNames.indices) {
                                val shelfName = shelfNames[i]
                                val isChecked = checkedItems[i]
                                val shelf = shelves[i]

                                if (isChecked) {
                                    if (!shelf.bookIds.contains(currentBook.key)) {
                                        db.shelfDAO().addBookIdToShelf(shelfName, currentBook.key)
                                        SyncManager.addBookIdToShelf(shelfName, currentBook.key)
                                        SyncManager.addBookToFirebase(currentBook)
                                        addedToShelf = true
                                    }
                                } else {
                                    if (shelf.bookIds.contains(currentBook.key)) {
                                        db.shelfDAO().removeBookIdFromShelf(shelfName, currentBook.key)
                                        SyncManager.removeBookIdFromShelf(shelfName, currentBook.key)
                                    }
                                }
                            }

                            if (addedToShelf){
                                SyncManager.addBookToFirebase(currentBook)
                                Toast.makeText(requireContext(), "teset", Toast.LENGTH_SHORT).show()
                            }

                            Toast.makeText(
                                requireContext(),
                                "Shelf selections updated!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    builder.setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }

                    builder.setNeutralButton("Create New Shelf") { _, _ ->
                        showCreateShelfDialog(currentBook)
                    }

                    builder.show()
                }
            }
        }

        btnSave.setOnClickListener {
            book?.let { currentBook ->
                lifecycleScope.launch {
                    val isFavourite = currentBook.isFavourite
                    if (!isFavourite) {
                        currentBook.isFavourite = true
                        bookDao.updateFavourite(currentBook.key,true)
                        btnSave.setImageResource(R.drawable.ic_saved)
                        Toast.makeText(requireContext(), "Book saved!", Toast.LENGTH_SHORT).show()

                        SyncManager.addBookToFirebase(currentBook)

                    } else {
                        currentBook.isFavourite = false
                        bookDao.updateFavourite(currentBook.key,false)
                        btnSave.setImageResource(R.drawable.ic_save)
                        Toast.makeText(requireContext(), "Book unsaved", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }



        btnShare.setOnClickListener {
            book?.let { currentBook ->
                val shareIntent = android.content.Intent().apply {
                    action = android.content.Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(
                        android.content.Intent.EXTRA_TEXT,
                        "Look at my book: ${currentBook.title}"
                    )
                }
                startActivity(
                    android.content.Intent.createChooser(
                        shareIntent,
                        "Share using:"
                    )
                )
            }
        }
    }

    private fun addBookToShelf(book: Book, shelfName: String) {
        lifecycleScope.launch {
            val exists = db.bookDao().countBookByKey(book.key) > 0
            if(!exists){
                db.bookDao().insert(book)
                SyncManager.addBookToFirebase(book)
            }
            val shelves = db.shelfDAO().getAllShelves()
            val shelfExists = shelves.any { it.shelfName == shelfName }

            if (!shelfExists){
                db.shelfDAO().insertShelf(Shelf(shelfName,emptyList()))
                SyncManager.addShelfToFirebase(Shelf(shelfName,emptyList()))
            }


            db.shelfDAO().addBookIdToShelf(shelfName, book.key)
            SyncManager.addBookIdToShelf(shelfName, book.key)



            Toast.makeText(requireContext(), "Added to $shelfName", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showCreateShelfDialog(book: Book) {
        val input = android.widget.EditText(requireContext())
        input.hint = "Enter shelf name"

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("New Shelf")
            .setView(input)
            .setPositiveButton("Create") { dialog, _ ->
                val shelfName = input.text.toString().trim()
                if (shelfName.isNotEmpty()) {
                    lifecycleScope.launch {
                        val newShelf = Shelf(shelfName, listOf(book.key))
                        db.shelfDAO().insertShelf(newShelf)
                        SyncManager.addShelfToFirebase(newShelf)
                        SyncManager.addBookIdToShelf(shelfName, book.key)

                        Toast.makeText(requireContext(), "Shelf '$shelfName' created!", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }


}