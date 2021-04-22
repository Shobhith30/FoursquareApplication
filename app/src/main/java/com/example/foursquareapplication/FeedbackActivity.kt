package com.example.foursquareapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.foursquareapplication.databinding.ActivityAddReviewBinding
import com.example.foursquareapplication.databinding.ActivityFeedbackBinding

class FeedbackActivity : AppCompatActivity() {

    private lateinit var feedBackBinding: ActivityFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)



        feedBackBinding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(feedBackBinding.root)

        setToolbar()

        val submit=findViewById<Button>(R.id.submit)

    }


    private fun setToolbar() {
        feedBackBinding.toolbar.setNavigationIcon(R.drawable.back_icon)
        feedBackBinding.toolbarTitle.text = "Feedback"
        feedBackBinding.toolbar.inflateMenu(R.menu.menu)
    }
}