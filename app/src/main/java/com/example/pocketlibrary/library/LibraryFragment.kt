package com.example.pocketlibrary.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.pocketlibrary.BookFragment
import com.example.pocketlibrary.BookToolbarFragment
import com.example.pocketlibrary.R

class LibraryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        val favouriteButton = view.findViewById<Button>(R.id.favourite_button)
        val localStorageButton = view.findViewById<Button>(R.id.localStorage_button)

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

        localStorageButton.setOnClickListener {
            val fragment = SavedBookFragment()
            //pass data
            val bundle = Bundle()
            bundle.putBoolean("isFavourite", false)
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.category_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        return view
    }
}