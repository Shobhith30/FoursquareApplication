package com.example.foursquareapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ActivityFeedbackBinding
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.Status
import com.example.foursquareapplication.viewmodel.FeedbackViewModel

class FeedbackActivity : AppCompatActivity() {

    private lateinit var feedBackBinding: ActivityFeedbackBinding
    private lateinit var feedbackViewModel : FeedbackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        feedBackBinding = ActivityFeedbackBinding.inflate(layoutInflater)
        feedbackViewModel = ViewModelProvider(this).get(FeedbackViewModel::class.java)
        setContentView(feedBackBinding.root)

        setToolbar()
        submitFeedBack()


    }
    private fun submitFeedBack(){
        feedBackBinding.submit.setOnClickListener{
            val feedback = feedBackBinding.addFeedback.text.toString().trim()
            if(feedback.isNotEmpty()) {
                val sharedPreferences =
                    getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE)
                val userId = sharedPreferences.getString(Constants.USER_ID, "")
                val token = sharedPreferences.getString(Constants.USER_TOKEN, "")
                if (userId != null && token != null) {
                    val userToken= "Bearer $token"
                    val feedback = hashMapOf("userId" to userId , "feedback" to feedback)
                    val submitFeedback = feedbackViewModel.submitFeedBack(feedback,userToken)
                    submitFeedback.observe(this){
                        when(it.status){
                            Status.LOADING ->{showProgressBar()}
                            Status.SUCCESS -> {
                                Toast.makeText(applicationContext, it.data?.getMessage(), Toast.LENGTH_SHORT).show()
                                hideProgressBar()
                                onBackPressed()
                            }
                            Status.ERROR -> {
                                Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                                hideProgressBar()
                            }
                        }
                    }
                }
            }else{
                Toast.makeText(applicationContext, "Feedback Cannot be Empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideProgressBar() {
        feedBackBinding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        feedBackBinding.progressBar.visibility = View.VISIBLE
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