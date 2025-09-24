//Adapter for recyclerview for books  at main page

package com.example.pocketlibrary.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.content.Intent

class BookLongAdapter(
    private var books : List<Book>,
    private val context : Context
): RecyclerView.Adapter<BookLongAdapter.BookViewHolder>(){
    class BookViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val textTitle : TextView = view.findViewById(R.id.textTitle)
        val bookImage : ImageView = view.findViewById(R.id.bookImage)

        val bookDesc : TextView = view.findViewById(R.id.textDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_long_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {

        val book = books[position]

        holder.textTitle.text = book.title
        holder.bookImage.setImageResource(book.imageId)
        holder.bookDesc.text = book.desc

        holder.itemView.setOnClickListener{
            val intent = Intent(context, BookDetailsActivity::class.java)
            intent.putExtra("book_details", book)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = books.size
    fun updateData(newList: List<Book>){
        books = newList
        notifyDataSetChanged()
    }
}