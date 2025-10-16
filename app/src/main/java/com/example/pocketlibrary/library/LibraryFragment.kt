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
            parentFragmentManager.beginTransaction()
                .replace(R.id.category_container, SavedBookFragment())
                .addToBackStack(null)
                .commit()
        }

        localStorageButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.category_container, SavedBookFragment())
                .addToBackStack(null)
                .commit()
        }
        return view
    }
}