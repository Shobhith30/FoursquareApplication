package com.example.foursquareapplication.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.TextView
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.FragmentSearchSuggestionsBinding
import com.example.foursquareapplication.ui.HomeActivity


class SearchSuggestionsFragment : Fragment() {

    private var _searchSuggestionBinding : FragmentSearchSuggestionsBinding? = null
    private val searchSuggestionBinding get() = _searchSuggestionBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _searchSuggestionBinding = FragmentSearchSuggestionsBinding.inflate(inflater,container,false)


        val names= arrayOf("Top pick","Popular","Lunch","Coffee")

        val arrayAdapter:ArrayAdapter<String> = ArrayAdapter(
            requireActivity(),android.R.layout.simple_list_item_1,names
        )

        searchSuggestionBinding.suggestionListView.adapter=arrayAdapter

        searchSuggestionBinding.suggestionListView.setOnItemClickListener { _, view, position, _ ->

            selectedSuggetion((view as TextView).text.toString())

        }

        return searchSuggestionBinding.root
    }

    private fun selectedSuggetion(value:String){
        val searchView = activity?.findViewById<androidx.appcompat.widget.SearchView>(R.id.search_place)
        searchView?.setQuery(value,true)
    }

}