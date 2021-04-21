package com.example.foursquareapplication

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.foursquareapplication.databinding.FragmentAboutUsBinding

class AboutUsFragment : Fragment() {

    private var _aboutUsBinding : FragmentAboutUsBinding? = null
    private val aboutUsBinding get() = _aboutUsBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _aboutUsBinding = FragmentAboutUsBinding.inflate(inflater,container,false)
        setToolbar()
        return aboutUsBinding.root
    }

    private fun setToolbar() {
        aboutUsBinding.toolbar.setNavigationIcon(R.drawable.back_icon)
        aboutUsBinding.toolbarTitle.text = getString(R.string.about_us)
        aboutUsBinding.toolbar.inflateMenu(R.menu.menu_about_us)
    }

}