package com.example.foursquareapplication.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.foursquareapplication.ui.NearYouFragment

internal class HomeTabAdapter(fm: FragmentManager, var totalTabs: Int): FragmentStatePagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
         when (position) {
            0 -> {
                val fragment =  NearYouFragment()
                val bundle = Bundle()
                bundle.putString("key","nearBy")
                fragment.arguments = bundle
                return fragment
            }
            1 ->{
                val fragment =  NearYouFragment()
                val bundle = Bundle()
                bundle.putString("key","topPick")
                fragment.arguments = bundle
                return fragment
            }
            2->{
                val fragment =  NearYouFragment()
                val bundle = Bundle()
                bundle.putString("key","topPick")
                fragment.arguments = bundle
                return fragment
            }
            3->{
                return NearYouFragment()
            }
            4->{
                return NearYouFragment()
            }
            else -> return getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}