package com.example.pocketlibrary.library

import androidx.fragment.app.FragmentManager
import android.content.Context
import android.widget.TextView
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.ImageView
import com.example.pocketlibrary.R
import com.example.pocketlibrary.Book
import com.bumptech.glide.Glide
import com.example.pocketlibrary.BookFragment

class SavedBookAdapter (
    private var books: List<Book>,
    private val context: Context,
    private val fragmentManager: FragmentManager
): RecyclerView.Adapter<SavedBookAdapter.BookViewHolder>() {

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textTitle: TextView = view.findViewById(R.id.textTitle)
        val textAuthor: TextView = view.findViewById(R.id.textAuthor)
        val bookImage: ImageView = view.findViewById(R.id.bookImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.textTitle.text = book.title
        holder.textAuthor.text = book.author.joinToString ( "," )

        //to load saved cover image using cover id
        book.coverId?.let{ coverId ->
            val coverUrl = "https://covers.openlibrary.org/b/id/${book.coverId}-M.jpg"
            Glide.with(context)
                .load(coverUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.bookImage)

            //to make the book clickable
            holder.itemView.setOnClickListener {
                val fragment = BookFragment.newInstance(book)
                fragmentManager.beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun getItemCount(): Int = books.size

    fun updateList(newBook: List<Book>) {
        books = newBook
        notifyDataSetChanged()
    }
}