package com.example.foursquareapplication.network

import com.example.foursquareapplication.model.FavouriteResponse
import com.example.foursquareapplication.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FavouriteApi {

    @GET("getFavourite")
    fun getFavourite(@Query("userId") userId : Int,
                     @Query("pageNo") pageNumber : Int,
                     @Query("pageSize") pageSize : Int,
                     @Header("Authorization") token : String) : Call<FavouriteResponse>
}