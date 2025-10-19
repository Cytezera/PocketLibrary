package com.example.pocketlibrary.discovery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pocketlibrary.Book
import com.example.pocketlibrary.BookFragment
import com.example.pocketlibrary.BookToolbarFragment
import com.example.pocketlibrary.R

class DiscoveryAdapter(
    private var books: List<Book>,
    private val context: Context,
    private val onBookClick: (Book) -> Unit
) : RecyclerView.Adapter<DiscoveryAdapter.BookViewHolder>() {

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textTitle: TextView = view.findViewById(R.id.textTitle)
        val bookImage: ImageView = view.findViewById(R.id.bookImage)
        val bookAuthor: TextView = view.findViewById(R.id.textAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_discovery_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]

        holder.textTitle.text = book.title

        val authors = book.author.joinToString(", ")
        val year = book.publishYear?.toString() ?: "-"
        holder.bookAuthor.text = "$authors • $year"

        if (book.coverId > 0) {
            val coverUrl = "https://covers.openlibrary.org/b/id/${book.coverId}-M.jpg"
            Glide.with(holder.bookImage.context)
                .load(coverUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.bookImage)
        } else {
            holder.bookImage.setImageResource(R.drawable.ic_launcher_foreground)
        }

        holder.bookImage.contentDescription = "${book.title} cover"
        holder.itemView.setOnClickListener { onBookClick(book) }
    }

    override fun getItemCount(): Int = books.size

    fun updateList(newList: List<Book>) {
        books = newList
        notifyDataSetChanged()
    }
}
