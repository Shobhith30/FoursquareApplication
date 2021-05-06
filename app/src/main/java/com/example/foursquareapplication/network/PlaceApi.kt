package com.example.foursquareapplication.network

import com.example.foursquareapplication.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceApi {

    @GET("PlaceApi/{type}")
    fun getPlace(@Path("type") type : String,
                 @Query("latitude") latitude: Double,
                 @Query("longitude") longitude :Double,
                 @Query("pageNo") pageNo :Int,
                 @Query("pageSize") pageSize : Int) : Call<PlaceResponse>
}