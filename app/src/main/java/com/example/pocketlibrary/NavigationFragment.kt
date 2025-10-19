package com.example.pocketlibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.pocketlibrary.create.CreateFragment
import com.example.pocketlibrary.discovery.DiscoveryContainerFragment
import com.example.pocketlibrary.home.HomeContainerFragment
import com.example.pocketlibrary.library.LibraryContainerFragment
import com.example.pocketlibrary.search.SearchFragment

class NavigationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val home: LinearLayout = view.findViewById(R.id.nav_home)
        val search: LinearLayout = view.findViewById(R.id.nav_search)
        val discovery: LinearLayout = view.findViewById(R.id.nav_discovery)
        val library: LinearLayout = view.findViewById(R.id.nav_library)
        val create: LinearLayout = view.findViewById(R.id.nav_create)

        home.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, HomeContainerFragment())
                .commit()
        }

        search.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, SearchFragment())
                .commit()
        }

        discovery.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, com.example.pocketlibrary.discovery.DiscoveryContainerFragment())
                .commit()
        }

        library.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, LibraryContainerFragment())
                .commit()
        }
        create.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, CreateFragment())
                .commit()
        }
    }
}