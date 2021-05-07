package com.example.foursquareapplication.network

import com.example.foursquareapplication.model.Rating
import com.example.foursquareapplication.model.Review
import retrofit2.Call
import retrofit2.http.*

interface ReviewApi {

    @GET("reviews")
    fun getReviews(@Query("PlaceId") placeId : Int,
                   @Query("pageNo") pageNo :Int,
                   @Query("pageSize") pageSize : Int) : Call<Review>

    @POST("addRating")
    fun addRating(@Header("Authorization") token : String, @Body rating : HashMap<String,String>) : Call<Rating>
}