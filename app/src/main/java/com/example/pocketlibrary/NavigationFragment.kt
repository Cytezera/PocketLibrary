package com.example.pocketlibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.widget.Button
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NavigationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NavigationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val home: LinearLayout = view.findViewById(R.id.nav_home)
        val search: LinearLayout = view.findViewById(R.id.nav_search)
        val discovery: LinearLayout = view.findViewById(R.id.nav_discovery)
        val library: LinearLayout = view.findViewById(R.id.nav_library)
        val create: LinearLayout = view.findViewById(R.id.nav_create)

        home.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, HomeFragment())
                .commit()
        }
        search.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, SearchFragment())
                .commit()
        }
        discovery.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, DiscoveryFragment())
                .commit()
        }
        library.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, LibraryFragment())
                .commit()
        }
        create.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, CreateFragment())
                .commit()
        }

    }
}