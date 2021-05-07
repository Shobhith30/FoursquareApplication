package com.example.foursquareapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.foursquareapplication.model.Review
import com.example.foursquareapplication.repository.ReviewRepository

class AddReviewViewModel(application: Application)  : AndroidViewModel(application) {
    private val addReviewRepository = ReviewRepository(application)

    fun addReview(token:String,user : HashMap<String,String>): LiveData<Review> {

        return addReviewRepository.addUserReview(token,user)
    }
}