package com.example.foursquareapplication.network

import com.example.foursquareapplication.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface PhotosApi {

    @GET("getPictures")
    fun getPictures(
        @Query("placeId")placeId:Int,
        @Query("pageNo")pageNo:Int,
        @Query("pageSize")pageSize:Int
    ) : Call<Photos>

    @GET("getPhoto")
    fun getPhotoDetails(
            @Header("Authorization") token:String,
            @Query("photoId") placeId: Int

    ) :Call<PhotoDetails>

    @Multipart
    @POST("uploadReviewImage")
    fun uploadReviewImage(
        @Query("placeId") placeId:Int,
        @Query("userId") userId:Int,
        @Header("Authorization") token:String,
        @Part files: ArrayList<MultipartBody.Part>
        ):Call<ApiResponse>

}