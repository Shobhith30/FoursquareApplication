package com.example.foursquareapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foursquareapplication.model.Feedback
import com.example.foursquareapplication.model.Resource
import com.example.foursquareapplication.repository.FeedbackRepository

class FeedbackViewModel : ViewModel() {

    private val feedbackRepository = FeedbackRepository()

    fun submitFeedBack(feedback : HashMap<String,String> , token : String): LiveData<Resource<Feedback>> {
        return feedbackRepository.submitFeedBack(feedback, token)
    }
}