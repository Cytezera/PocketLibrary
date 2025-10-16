package com.example.pocketlibrary.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.pocketlibrary.R

class HomeContainerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (childFragmentManager.findFragmentById(R.id.toolbar_container) == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.toolbar_container, HomeToolbarFragment())
                .replace(R.id.content_container, HomeBodyFragment()) // was HomeFragment before
                .commit()
        }
    }
}
