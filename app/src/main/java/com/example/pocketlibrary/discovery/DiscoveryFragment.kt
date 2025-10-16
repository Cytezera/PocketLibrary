package com.example.pocketlibrary.discovery

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketlibrary.Book
import com.example.pocketlibrary.BookFragment
import com.example.pocketlibrary.BookToolbarFragment
import com.example.pocketlibrary.R
import com.example.pocketlibrary.databinding.FragmentDiscoveryBinding
import com.example.pocketlibrary.search.BookListAdapter
import com.google.api.Context
import kotlinx.coroutines.flow.collectLatest

class DiscoveryFragment : Fragment() {
    private var _b: FragmentDiscoveryBinding? = null
    private val b get() = _b!!
    private val vm: DiscoverViewModel by viewModels()
    private var listState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentDiscoveryBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = DiscoveryAdapter(emptyList(), requireContext()) { book: Book ->
            val fragment = BookFragment.newInstance(book)
            val toolbarFragment = BookToolbarFragment.newInstance(book)
            requireParentFragment().childFragmentManager.beginTransaction()
                .replace(R.id.category_container, fragment)
                .replace(R.id.toolbar_container, toolbarFragment)
                .addToBackStack(null)
                .commit()
        }

        b.recycler.adapter = adapter
        b.recycler.layoutManager = if (isLandscape())
            GridLayoutManager(requireContext(), 2)
        else
            LinearLayoutManager(requireContext())

        b.swipe.setOnRefreshListener { vm.reload() }
        b.btnNext.setOnClickListener { vm.nextTopic() }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.state.collectLatest { s ->
                b.swipe.isRefreshing = s.loading
                b.tvError.visibility = if (s.error != null) View.VISIBLE else View.GONE
                b.tvError.text = s.error ?: ""
                b.tvEmpty.visibility = if (!s.loading && s.items.isEmpty() && s.error == null) View.VISIBLE else View.GONE

                val saved = listState
                adapter.updateList(s.items)
                // restore scroll AFTER adapter update
                saved?.let { b.recycler.layoutManager?.onRestoreInstanceState(it) }
                listState = null
            }
        }
    }

    override fun onPause() {
        super.onPause()
        listState = b.recycler.layoutManager?.onSaveInstanceState()
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }

    private fun isLandscape() =
        resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}