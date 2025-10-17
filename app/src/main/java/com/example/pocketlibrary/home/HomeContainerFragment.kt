package com.example.pocketlibrary.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.pocketlibrary.R
import com.example.pocketlibrary.search.BookListFragment
import com.example.pocketlibrary.search.SearchBarFragment

class HomeContainerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_container, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction()
            .replace(R.id.home_toolbar_container, HomeToolbarFragment())
            .replace(R.id.home_book_container, HomeBodyFragment())
            .commit()
    }


}
