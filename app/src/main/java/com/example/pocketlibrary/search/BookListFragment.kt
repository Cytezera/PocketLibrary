package com.example.pocketlibrary.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketlibrary.R
import com.example.pocketlibrary.Book
import com.example.pocketlibrary.BookViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BookListFragment : Fragment() {

    private lateinit var adapter: BookListAdapter
    private val bookViewModel: BookViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.bookRecylerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = BookListAdapter(emptyList(), requireContext())
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            bookViewModel.state.collectLatest { uiState ->
                adapter.updateList(
                    uiState.results.map { doc ->
                        Book(
                            key = doc.key ?: "unknown_${doc.coverId ?: 0}",
                            title = doc.title ?: "Unknown Title",
                            author = doc.authorNames ?: listOf("Unknown Author"),
                            coverId = doc.coverId ?: 0,
                            publishYear = doc.publishYear ?: 0
                        )
                    }
                )
            }
        }
    }
}
