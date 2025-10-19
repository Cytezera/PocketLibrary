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
import com.example.pocketlibrary.R
import com.example.pocketlibrary.internalDatabase.AppDatabase
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SavedBookFragment : Fragment() {

    private lateinit var viewModel: SavedBookViewModel
    private lateinit var adapter: SavedBookAdapter
    private var isFavouriteFlag: Boolean = false
    private var shelfName: String? = null
    private lateinit var db: AppDatabase

    //receiving the data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFavouriteFlag = arguments?.getBoolean("isFavourite") ?: false
        shelfName = arguments?.getString("shelfName")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saved_book, container, false)

        val searchBar = view.findViewById<SearchView>(R.id.search_bar)
        val backButton = view.findViewById<Button>(R.id.back_button)
        val recyclerView = view.findViewById<RecyclerView>(R.id.savedBookRecyclerView)

        adapter = SavedBookAdapter(emptyList(), requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this)[SavedBookViewModel::class.java]
        db = AppDatabase.getDatabase(requireContext())
        val shelfDao = db.shelfDAO()
        viewModel.allBooks.observe(viewLifecycleOwner) { books ->
            viewLifecycleOwner.lifecycleScope.launch {
                val filteredBooks = if (isFavouriteFlag) {
                    // Show only favourites
                    books.filter { it.isFavourite }

                } else if (!shelfName.isNullOrEmpty()) {
                    val shelf = db.shelfDAO().getShelfByName(shelfName!!) //idk magai gui zomai use shelfName!! but its fine hahahaa
                    val shelfBookIds = shelf?.bookIds ?: emptyList()
                    books.filter { it.key in shelfBookIds }

                } else {
                    //just in case
                    books
                }
                adapter.updateList(filteredBooks)
            }
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