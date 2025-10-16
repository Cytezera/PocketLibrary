package com.example.pocketlibrary.home

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketlibrary.R
import com.example.pocketlibrary.create.CreateFragment
import com.example.pocketlibrary.databinding.FragmentHomeBinding
import com.example.pocketlibrary.discovery.DiscoveryFragment
import com.example.pocketlibrary.library.LibraryContainerFragment
import com.example.pocketlibrary.library.SavedBookAdapter
import com.example.pocketlibrary.library.SavedBookViewModel
import com.example.pocketlibrary.search.SearchFragment

class HomeFragment : Fragment() {

    private var _b: FragmentHomeBinding? = null
    private val b get() = _b!!

    private val savedVm: SavedBookViewModel by activityViewModels()
    private lateinit var adapter: SavedBookAdapter
    private var listState: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentHomeBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        b.actionSearch.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, SearchFragment())
                .addToBackStack(null)
                .commit()
        }
        b.actionLibrary.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, LibraryContainerFragment())
                .addToBackStack(null)
                .commit()
        }
        b.actionAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, CreateFragment())
                .addToBackStack(null)
                .commit()
        }
        b.actionDiscover.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, DiscoveryFragment())
                .addToBackStack(null)
                .commit()
        }




        adapter = SavedBookAdapter(emptyList(), requireContext())
        b.recyclerRecent.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        b.recyclerRecent.adapter = adapter
        b.recyclerRecent.setHasFixedSize(true)


        savedVm.allBooks.observe(viewLifecycleOwner) { list ->
            if (list.isNullOrEmpty()) {
                b.textEmpty.visibility = View.VISIBLE
                adapter.updateList(emptyList())
            } else {
                b.textEmpty.visibility = View.GONE
                // Limit to 12 to keep Home snappy
                adapter.updateList(list.take(12))
                // Restore scroll if we have it
                listState?.let { state ->
                    b.recyclerRecent.layoutManager?.onRestoreInstanceState(state)
                    listState = null
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val state = b.recyclerRecent.layoutManager?.onSaveInstanceState()
        if (state != null) outState.putParcelable("home_recent_state", state)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        listState = savedInstanceState?.getParcelable("home_recent_state")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
