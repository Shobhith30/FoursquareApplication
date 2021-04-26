package com.example.foursquareapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquareapplication.databinding.FragmentSearchFilterBinding
import com.example.foursquareapplication.databinding.FragmentSearchResultBinding

class SearchFilterFragment : Fragment() {

    private var _filterBinding : FragmentSearchFilterBinding? = null
    private val filterBinding get() = _filterBinding!!
    private lateinit var featureAdapter : FeatureAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _filterBinding = FragmentSearchFilterBinding.inflate(inflater,container,false)
        initializeFeatureRecyclerView()
        return filterBinding.root

    }

    private fun initializeFeatureRecyclerView() {
        val layoutManager = LinearLayoutManager(this.requireContext())
        filterBinding.featureRecyclerView.layoutManager = layoutManager
        featureAdapter = FeatureAdapter(Constants.featureList)
        filterBinding.featureRecyclerView.adapter = featureAdapter
    }

}