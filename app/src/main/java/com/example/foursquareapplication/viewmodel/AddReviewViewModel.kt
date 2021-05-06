package com.example.foursquareapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.foursquareapplication.model.Review
import com.example.foursquareapplication.model.User
import com.example.foursquareapplication.repository.AddReviewRepository
import com.example.foursquareapplication.repository.MainRepository

class AddReviewViewModel(application: Application)  : AndroidViewModel(application) {
    private val addReviewRepository = AddReviewRepository(application)

    fun addReview(token:String,user : HashMap<String,String>): LiveData<Review> {

        return addReviewRepository.addUserReview(token,user)
    }
}