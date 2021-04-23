package com.example.foursquareapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

internal class HomeTabAdapter(fm: FragmentManager, var totalTabs: Int): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                NearYouFragment()
            }
            1 ->{
                NearYouFragment()
            }
            2->{
                NearYouFragment()
            }
            3->{
                NearYouFragment()
            }
            4->{
                NearYouFragment()
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}