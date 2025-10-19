package com.example.pocketlibrary.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketlibrary.BookFragment
import com.example.pocketlibrary.BookToolbarFragment
import com.example.pocketlibrary.R
import com.example.pocketlibrary.Shelf
import com.example.pocketlibrary.internalDatabase.AppDatabase
import kotlinx.coroutines.launch

class LibraryFragment : Fragment() {
    private lateinit var shelfAdapter: ShelfAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        val favouriteButton = view.findViewById<Button>(R.id.favourite_button)

        val shelfRecyclerView = view.findViewById<RecyclerView>(R.id.shelfRecyclerView)

        shelfAdapter = ShelfAdapter(parentFragmentManager,requireContext())
        shelfRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        shelfRecyclerView.adapter = shelfAdapter

        val shelfDAO = AppDatabase.getDatabase(requireContext()).shelfDAO()
        shelfDAO.getAllShelvesCategory().observe(viewLifecycleOwner) { shelves ->
            shelfAdapter.submitList(shelves)
        }

        //change to SavedBookFragment
        favouriteButton.setOnClickListener {
            val fragment = SavedBookFragment()
            //pass data
            val bundle = Bundle()
            bundle.putBoolean("isFavourite", true)
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.category_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        return view
    }
}