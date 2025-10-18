package com.example.pocketlibrary.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketlibrary.Book
import com.example.pocketlibrary.R
import androidx.fragment.app.FragmentManager
import com.example.pocketlibrary.Shelf
import android.widget.Button
import android.os.Bundle

class ShelfAdapter (
    private val fragmentManager: FragmentManager
) : ListAdapter<Shelf, ShelfAdapter.ShelfViewHolder>(DiffCallback()) {

    class ShelfViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val shelfButton: Button = view.findViewById(R.id.btnAddToShelf)
    }

    class DiffCallback : DiffUtil.ItemCallback<Shelf>() {
        override fun areItemsTheSame(oldItem: Shelf, newItem: Shelf) = oldItem.shelfName == newItem.shelfName
        override fun areContentsTheSame(oldItem: Shelf, newItem: Shelf) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelfViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shelves_button, parent, false)
        return ShelfViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShelfViewHolder, position: Int) {
        val shelf = getItem(position)
        holder.shelfButton.text = shelf.shelfName

        holder.shelfButton.setOnClickListener {
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