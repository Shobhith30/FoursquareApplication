package com.example.foursquareapplication.network

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

    @POST("addFavourite")
    fun addToFavourite(@Header("Authorization") token : String,
                       @Body favourite : HashMap<String,String> ) : Call<FavouriteResponse>

    @DELETE("deleteFavourite")
    fun deleteFavourite(@Header("Authorization") token : String,
                        @Body favourite : HashMap<String,String> ) : Call<FavouriteResponse>
}