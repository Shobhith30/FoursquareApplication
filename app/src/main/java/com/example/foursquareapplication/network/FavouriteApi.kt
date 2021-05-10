package com.example.foursquareapplication.network

import com.example.foursquareapplication.model.AddFavouriteResponse
import com.example.foursquareapplication.model.FavouriteResponse
import com.example.foursquareapplication.model.PlaceResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface FavouriteApi {

    @GET("getFavourite")
     fun getFavourite(@Query("userId") userId : Int,
                     @Query("pageNo") pageNumber : Int,
                     @Query("pageSize") pageSize : Int,
                     @Header("Authorization") token : String) : Call<FavouriteResponse>

    @GET("getFavourite")
    suspend fun getFavouriteData(@Query("userId") userId : Int,
                             @Query("pageNo") pageNumber : Int,
                             @Query("pageSize") pageSize : Int,
                             @Header("Authorization") token : String) : FavouriteResponse

    @POST("addFavourite")
    fun addToFavourite(@Header("Authorization") token : String,
                       @Body favourite : HashMap<String,String> ) : Call<AddFavouriteResponse>

    @HTTP(method = "DELETE", path = "deleteFavourite", hasBody = true)
    fun deleteFavourite(@Header("Authorization") token : String,
                        @Body favourite : HashMap<String,String> ) : Call<FavouriteResponse>
}