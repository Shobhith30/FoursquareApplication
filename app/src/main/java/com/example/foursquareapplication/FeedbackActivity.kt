package com.example.foursquareapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foursquareapplication.databinding.ActivityFeedbackBinding

class FeedbackActivity : AppCompatActivity() {

    private lateinit var feedBackBinding: ActivityFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        feedBackBinding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(feedBackBinding.root)

        setToolbar()
        submitFeedBack()


    }
    private fun submitFeedBack(){
        feedBackBinding.submit.setOnClickListener{
            startActivity(Intent(this,HomeActivity::class.java))
        }
    }


    private fun setToolbar() {
        feedBackBinding.toolbar.setNavigationIcon(R.drawable.back_icon)
        feedBackBinding.toolbarTitle.text = getString(R.string.feedback)
        feedBackBinding.toolbar.inflateMenu(R.menu.menu_home_icon)
        feedBackBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        feedBackBinding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.home -> onBackPressed()
            }
            true
        }
    }
}