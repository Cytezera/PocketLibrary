
package com.example.pocketlibrary.home

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
import com.example.pocketlibrary.BookToolbarFragment

class HistoryAdapter(
    private var books : List<Book>,
    private val context : Context,
    private val fragmentManager: FragmentManager
): RecyclerView.Adapter<HistoryAdapter.BookViewHolder>(){
    class BookViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val bookImage : ImageView = view.findViewById(R.id.bookImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {

        val book = books[position]

        if (!book.coverUri.isNullOrEmpty()) {
            Glide.with(context)
                .load(book.coverUri)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(holder.bookImage)

        } else if (book.coverId != 0) {
            val coverUrl = "https://covers.openlibrary.org/b/id/${book.coverId}-M.jpg"
            Glide.with(context)
                .load(coverUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(holder.bookImage)
        } else {
            holder.bookImage.setImageResource(R.drawable.ic_placeholder)
        }

        holder.itemView.setOnClickListener{
            val fragment = BookFragment.newInstance(book)
            val fragment2 = BookToolbarFragment.newInstance(book)

            fragmentManager.beginTransaction()
                .replace(R.id.home_toolbar_container, fragment2)

                .replace(R.id.home_book_container, fragment)
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