package com.example.foursquareapplication.network

import com.example.foursquareapplication.model.Feedback
import com.example.foursquareapplication.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FeedbackApi {

    @POST("feedback")
    fun submitFeedback(@Body feedback : HashMap<String,String>, @Header("Authorization") token : String) : Call<Feedback>
}