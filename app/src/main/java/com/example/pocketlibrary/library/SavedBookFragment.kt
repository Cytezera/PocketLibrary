package com.example.pocketlibrary.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Query
import com.example.pocketlibrary.R

class SavedBookFragment : Fragment() {

    private lateinit var viewModel: SavedBookViewModel
    private lateinit var adapter: SavedBookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saved_book, container, false)

        val searchBar = view.findViewById<SearchView>(androidx.appcompat.R.id.search_bar)
        val backButton = view.findViewById<Button>(R.id.back_button)
        val recyclerView = view.findViewById<RecyclerView>(R.id.savedBookRecyclerView)

        adapter = SavedBookAdapter(emptyList(), requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this)[SavedBookViewModel::class.java]
        viewModel.allBooks.observe(viewLifecycleOwner) { books ->
            adapter.updateList(books)
        }

        //to search offline
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                performSearch(newText)
                return true
            }
        })

        backButton.setOnClickListener {
            //to go back to the previous fragment
            parentFragmentManager.popBackStack()
        }
        return view
    }

    private fun performSearch(query: String?) {
        val searchQuery = query?.trim()
        if(searchQuery.isNullOrEmpty()) {
            //show all the books
            viewModel.allBooks.observe(viewLifecycleOwner) { books ->
                adapter.updateList(books)
            }
        } else {
            //filtered
            viewModel.search(searchQuery).observe(viewLifecycleOwner) { books ->
                adapter.updateList(books)
            }
        }
    }
}