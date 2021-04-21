package com.example.foursquareapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foursquareapplication.databinding.ActivityReviewBinding

class ReviewActivity : AppCompatActivity() {

    private lateinit var reviewBinding: ActivityReviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reviewBinding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(reviewBinding.root)

        setToolbar()
    }

    private fun setToolbar() {
        reviewBinding.toolbar.setNavigationIcon(R.drawable.back_icon)
        reviewBinding.toolbarTitle.text = "Attil"
        reviewBinding.toolbar.inflateMenu(R.menu.menu_about_us)
    }
}