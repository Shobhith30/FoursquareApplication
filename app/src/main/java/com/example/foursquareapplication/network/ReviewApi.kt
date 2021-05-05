package com.example.foursquareapplication.network

import com.example.foursquareapplication.model.Review
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ReviewApi {

    @GET("reviews")
    fun getReviews(@Query("PlaceId") placeId : Int,
                   @Query("pageNo") pageNo :Int,
                   @Query("pageSize") pageSize : Int) : Call<Review>
}