package com.example.pocketlibrary.home

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketlibrary.R
import com.example.pocketlibrary.create.CreateFragment
import com.example.pocketlibrary.discovery.DiscoveryFragment
import com.example.pocketlibrary.internalDatabase.AppDatabase
import com.example.pocketlibrary.library.LibraryContainerFragment
import com.example.pocketlibrary.library.SavedBookViewModel
import com.example.pocketlibrary.search.BookListAdapter

class HomeBodyFragment : Fragment() {

    private lateinit var adapter: HomeBookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_body, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDatabase.getDatabase(requireContext())
        val bookDao = db.bookDao()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerRecent)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = HomeBookAdapter(emptyList(), requireContext(), parentFragmentManager)
        recyclerView.adapter = adapter

        bookDao.getLatestBooks(12).observe(viewLifecycleOwner) { books ->
            val favourites = books.filter { it.isFavourite }
            adapter.updateList(favourites)
        }
    }
}