package com.example.pocketlibrary.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.pocketlibrary.R

class HomeToolbarFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home_toolbar, container, false)
    }
}
