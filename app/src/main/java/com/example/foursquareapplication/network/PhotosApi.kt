package com.example.foursquareapplication.network

import android.graphics.Bitmap
import com.example.foursquareapplication.model.Photos
import com.example.foursquareapplication.model.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface PhotosApi {

    @GET("getPictures")
    fun getPictures(
        @Query("placeId")placeId:Int,
        @Query("pageNo")pageNo:Int,
        @Query("pageSize")pageSize:Int
    ) : Call<Photos>

    @Multipart
    @POST("uploadReviewImage")
    fun uploadReviewImage(
        @Query("placeId") placeId:Int,
        @Query("userId") userId:Int,
        @Header("Authorization") token:String,
        @Part files:ArrayList<MultipartBody.Part>
        ):Call<User>

}