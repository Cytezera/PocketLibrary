package com.example.pocketlibrary.library

import androidx.fragment.app.FragmentManager
import android.content.Context
import android.widget.TextView
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.rememberUpdatedState
import com.example.pocketlibrary.R
import com.example.pocketlibrary.Book
import com.bumptech.glide.Glide
import com.example.pocketlibrary.BookFragment
import com.example.pocketlibrary.BookToolbarFragment

class SavedBookAdapter (
    private var books: List<Book>,
    private val context: Context,
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
                val toolbarFragment = BookToolbarFragment.newInstance(book)

                //if curr screen can hold fragments, continue, if not, then do ntg
                val activity = context as? AppCompatActivity ?: return@setOnClickListener

                //add, replace or rmv fragments
                val fm = activity.supportFragmentManager

                //if true, put in the fragments | if not, then show main fragment in main container 
                val hasLibraryContainer =
                    activity.findViewById<View?>(R.id.category_container) != null &&
                    activity.findViewById<View?>(R.id.toolbar_container) != null

                if (hasLibraryContainer) {
                    fm.beginTransaction()
                        .replace(R.id.category_container, fragment)
                        .replace(R.id.toolbar_container, toolbarFragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    fm.beginTransaction()
                        .replace(R.id.main_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }

                /*activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.category_container, fragment)
                    .replace(R.id.toolbar_container, toolbarFragment)
                    .addToBackStack(null)
                    .commit()*/
            }
        }
    }

    override fun getItemCount(): Int = books.size

    fun updateList(newBook: List<Book>) {
        books = newBook
        notifyDataSetChanged()
    }
}