package com.example.pocketlibrary.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketlibrary.R
import com.example.pocketlibrary.internalDatabase.AppDatabase
import com.example.pocketlibrary.library.SavedBookViewModel
import kotlin.getValue
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

import android.util.Log


class HomeToolbarFragment : Fragment() {
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_toolbar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDatabase.getDatabase(requireContext())
        val bookDao = db.bookDao()


        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerHistory)

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        adapter = HistoryAdapter(emptyList(), requireContext(), parentFragmentManager)
        recyclerView.adapter = adapter



        lifecycleScope.launch {
            val history = db.historyDao().getLatestHistory(12)
            Log.d("HomeToolbarFragment", "History: $history")

            val bookIds = history.map { it.bookId }
            val booksFromDb = db.bookDao().getBooksByIds(bookIds)

            val books = bookIds.mapNotNull { id -> booksFromDb.find { it.key == id } }

            adapter.updateList(books)
        }

    }

}
