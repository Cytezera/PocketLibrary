package com.example.pocketlibrary.discovery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketlibrary.R
import com.example.pocketlibrary.library.LibraryFragment
import com.example.pocketlibrary.search.BookListFragment
import com.example.pocketlibrary.search.SearchBarFragment

class DiscoveryContainerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discovery_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (childFragmentManager.findFragmentById(R.id.category_container) == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.category_container, DiscoveryFragment())
                .commit()
        }
    }
}