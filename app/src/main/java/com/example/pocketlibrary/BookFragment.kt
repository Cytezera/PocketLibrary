package com.example.pocketlibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.pocketlibrary.internalDatabase.AppDatabase
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.widget.Toast



class BookFragment : Fragment() {
    private var book: Book? = null
    companion object {
        private const val ARG_BOOK = "arg_book"

        fun newInstance(book: Book): BookFragment {
            val fragment = BookFragment()
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
        return inflater.inflate(R.layout.fragment_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title: TextView = view.findViewById(R.id.bookTitle)
        val author: TextView = view.findViewById(R.id.bookAuthor)
        val year: TextView = view.findViewById(R.id.bookYear)
        val bookImage: ImageView = view.findViewById(R.id.bookImage)

        val b = book
        if (b != null) {
            title.text = b.title
            author.text = b.author.joinToString(", ")
            year.text = b.publishYear.toString()

            if (!b.coverUri.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(b.coverUri)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(bookImage)

            } else if (b.coverId != 0) {
                val coverUrl = "https://covers.openlibrary.org/b/id/${b.coverId}-M.jpg"
                Glide.with(requireContext())
                    .load(coverUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(bookImage)

            } else {
                bookImage.setImageResource(R.drawable.ic_placeholder)
            }

            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(requireContext())



                val exists = db.bookDao().countBookByKey(b.key) > 0

                if (!exists){
                    b.isFavourite = false
                    db.bookDao().insert(b)
                }

                db.historyDao().deleteByBookId(b.key)
                db.historyDao().insert(History(bookId = b.key))
            }


        }
    }


}