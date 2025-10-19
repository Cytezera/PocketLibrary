package com.example.pocketlibrary.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pocketlibrary.Book
import com.example.pocketlibrary.BookFragment
import com.example.pocketlibrary.BookToolbarFragment
import com.example.pocketlibrary.History
import com.example.pocketlibrary.R
import com.example.pocketlibrary.internalDatabase.AppDatabase
import kotlinx.coroutines.launch
import androidx.fragment.app.FragmentManager

class BookListAdapter(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val lifecycleOwner: androidx.lifecycle.LifecycleOwner
) : ListAdapter<Book, BookListAdapter.BookViewHolder>(DiffCallback()) {

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textTitle: TextView = view.findViewById(R.id.textTitle)
        val bookImage: ImageView = view.findViewById(R.id.bookImage)
        val bookAuthor: TextView = view.findViewById(R.id.textAuthor)
    }

    class DiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book) = oldItem.key == newItem.key
        override fun areContentsTheSame(oldItem: Book, newItem: Book) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.textTitle.text = book.title
        holder.bookAuthor.text = book.author.joinToString(", ")

        val coverUrl = when {
            !book.coverUri.isNullOrEmpty() -> book.coverUri
            book.coverId != 0 -> "https://covers.openlibrary.org/b/id/${book.coverId}-M.jpg"
            else -> null
        }

        Glide.with(context)
            .load(coverUrl)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .centerCrop()
            .thumbnail(0.1f)
            .into(holder.bookImage)

        holder.itemView.setOnClickListener {

            fragmentManager.beginTransaction()
                .replace(R.id.searchBar_container, BookToolbarFragment.newInstance(book))
                .replace(R.id.bookList_container, BookFragment.newInstance(book))
                .addToBackStack(null)
                .commit()
        }
    }
}