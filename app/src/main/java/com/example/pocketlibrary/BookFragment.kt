package com.example.pocketlibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        val bookImage : ImageView = view.findViewById(R.id.bookImage)

        val b = book
        if (b != null){
            title.text = b.title
            author.text = b.author.joinToString(", ")
            year.text = b.publishYear.toString()

            b.coverId?.let{ coverId ->
                val coverUrl = "https://covers.openlibrary.org/b/id/$coverId-M.jpg"
                Glide.with(requireContext())
                    .load(coverUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(bookImage)

            }
        }


    }


}