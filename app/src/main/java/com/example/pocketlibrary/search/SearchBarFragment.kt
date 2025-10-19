package com.example.pocketlibrary.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.pocketlibrary.BookViewModel
import com.example.pocketlibrary.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchBarFragment : Fragment() {

    private val bookViewModel: BookViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_bar, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        searchView.queryHint = "Books or Author"
        searchView.setIconifiedByDefault(false)

        // Debounced search
        var searchJob: Job? = null
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(300) // debounce 300ms
                    bookViewModel.updateQuery(newText.orEmpty())
                }
                return true
            }
        })

        // Observe loading state
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            bookViewModel.state.collect { uiState ->
                progressBar.visibility = if (uiState.loading) View.VISIBLE else View.GONE
            }
        }
    }
}