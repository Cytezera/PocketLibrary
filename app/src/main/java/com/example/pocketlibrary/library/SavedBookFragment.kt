package com.example.pocketlibrary.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val backButton = view.findViewById<Button>(R.id.back_button)
        val recyclerView = view.findViewById<RecyclerView>(R.id.savedBookRecyclerView)

        adapter = SavedBookAdapter(emptyList(), requireContext(), parentFragmentManager)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this)[SavedBookViewModel::class.java]
        viewModel.allBooks.observe(viewLifecycleOwner) { books ->
            adapter.updateList(books)
        }

        backButton.setOnClickListener {
            //to go back to the previous fragment
            requireActivity().supportFragmentManager.popBackStack()
        }
        return view
    }
}