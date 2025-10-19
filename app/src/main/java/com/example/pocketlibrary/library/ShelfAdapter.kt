package com.example.pocketlibrary.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pocketlibrary.R
import com.example.pocketlibrary.Shelf
import com.example.pocketlibrary.internalDatabase.BookDAO
import android.os.Bundle
import com.example.pocketlibrary.internalDatabase.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context

class ShelfAdapter(
    private val fragmentManager: FragmentManager,
    private val context: Context
) : ListAdapter<Shelf, ShelfAdapter.ShelfViewHolder>(DiffCallback()) {

    class ShelfViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageCategory)
        val categoryName: TextView = view.findViewById(R.id.textCategoryName)
        val bookCount: TextView = view.findViewById(R.id.textBookCount)
    }

    class DiffCallback : DiffUtil.ItemCallback<Shelf>() {
        override fun areItemsTheSame(oldItem: Shelf, newItem: Shelf) = oldItem.shelfName == newItem.shelfName
        override fun areContentsTheSame(oldItem: Shelf, newItem: Shelf) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelfViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ShelfViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShelfViewHolder, position: Int) {
        val bookDao = AppDatabase.getDatabase(context).bookDao()

        val shelf = getItem(position)
        holder.categoryName.text = shelf.shelfName
        val bookNum = shelf.bookIds.size
        holder.bookCount.text = "$bookNum books"

        CoroutineScope(Dispatchers.IO).launch {
            val books = shelf.bookIds.take(4).mapNotNull { bookDao.getBookByKey(it) }

            withContext(Dispatchers.Main) { // chu shi liao here idk why
                if (books.isNotEmpty()) {
                    val book = books.first()

                    val coverUrl = when {
                        !book.coverUri.isNullOrEmpty() -> book.coverUri
                        book.coverId != 0 -> "https://covers.openlibrary.org/b/id/${book.coverId}-M.jpg"
                        else -> null
                    }
                    Glide.with(holder.image.context)
                        .load(coverUrl)
                        .placeholder(R.drawable.placeholder_cover)
                        .into(holder.image)
                } else {
                    holder.image.setImageResource(R.drawable.placeholder_cover)
                }
            }
        }


        holder.itemView.setOnClickListener {
            val fragment = SavedBookFragment()
            val bundle = Bundle().apply {
                putString("shelfName", shelf.shelfName)
            }
            fragment.arguments = bundle

            fragmentManager.beginTransaction()
                .replace(R.id.category_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}