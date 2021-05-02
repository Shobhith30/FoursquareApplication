package com.example.foursquareapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private lateinit var aboutUsBinding : ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aboutUsBinding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(aboutUsBinding.root)
        setToolbar()
    }


    private fun setToolbar() {
        aboutUsBinding.toolbar.setNavigationIcon(R.drawable.back_icon)
        aboutUsBinding.toolbarTitle.text = getString(R.string.about_us)
        aboutUsBinding.toolbar.inflateMenu(R.menu.menu_home_icon)
        aboutUsBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        aboutUsBinding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.home -> onBackPressed()
            }
            true
        }
    }

}