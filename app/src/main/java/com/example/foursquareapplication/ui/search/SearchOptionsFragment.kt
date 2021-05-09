package com.example.foursquareapplication.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.FragmentSearchOptionsBinding

class SearchOptionsFragment : Fragment() {

    private lateinit var searchOptionBinding : FragmentSearchOptionsBinding
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        searchOptionBinding = FragmentSearchOptionsBinding.inflate(inflater,container,false)

        return  searchOptionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchOptionBinding.nearby.setOnClickListener {
            selectedSuggestion("Near By")
        }
    }

    private fun selectedSuggestion(query: String) {
        val searchView = activity?.findViewById<SearchView>(R.id.near_me_search)
        searchView?.setQuery(query,false)

    }
}