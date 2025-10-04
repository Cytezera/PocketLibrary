//Adapter for recyclerview for books  at main page

package com.example.pocketlibrary.search

import android.content.Context
import android.view.LayoutInflater
import com.example.pocketlibrary.Book
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.content.Intent
import com.bumptech.glide.Glide
import com.example.pocketlibrary.R
import androidx.fragment.app.FragmentManager
import com.example.pocketlibrary.BookFragment



class BookListAdapter(
    private var books : List<Book>,
    private val context : Context,
    private val fragmentManager: FragmentManager
): RecyclerView.Adapter<BookListAdapter.BookViewHolder>(){
    class BookViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val textTitle : TextView = view.findViewById(R.id.textTitle)
        val bookImage : ImageView = view.findViewById(R.id.bookImage)

        val bookAuthor : TextView = view.findViewById(R.id.textAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {


        val book = books[position]

        holder.textTitle.text = book.title

        book.coverId?.let{ coverId ->
            val coverUrl = "https://covers.openlibrary.org/b/id/$coverId-M.jpg"
            Glide.with(context)
                .load(coverUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.bookImage)

        }


        holder.bookAuthor.text = book.author.joinToString(", ")

        holder.itemView.setOnClickListener{
            val fragment = BookFragment.newInstance(book)
            fragmentManager.beginTransaction()
                .replace(R.id.bookList_container, fragment)
                .addToBackStack(null)
                .commit()

        }
    }

    override fun getItemCount(): Int = books.size
    fun updateList(newList: List<Book>){
        books = newList
        notifyDataSetChanged()
    }
}