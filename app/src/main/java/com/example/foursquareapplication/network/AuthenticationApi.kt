package com.example.foursquareapplication.network

import android.provider.ContactsContract
import com.example.foursquareapplication.model.Photos
import com.example.foursquareapplication.model.Review
import com.example.foursquareapplication.model.User
import retrofit2.Call
import retrofit2.http.*

interface AuthenticationApi {


    @POST("register")
    fun registerUser(@Body user : HashMap<String,String>) : Call<User>

    @POST("authenticate")
    fun authenticateUser(@Body user : HashMap<String,String>) : Call<User>

    @POST("generateOtp")
    fun generateOtp(@Body email : HashMap<String,String>) : Call<User>

    @POST("addReview")
    fun addReview(
        @Header("Authorization") token:String,
        @Body review : HashMap<String,String>
    ) : Call<Review>


    @PUT("validateOtp")
    fun validateOtp(@Body otp: HashMap<String, String>) : Call<User>

    @PUT("changePassword")
    fun confirmPassword( @Body password : HashMap<String,String>) : Call<User>

    @GET("getPictures")
    fun getPictures(
        @Query("placeId")placeId:Int,
        @Query("pageNo")pageNo:Int,
        @Query("pageSize")pageSize:Int
    ) :Call<Photos>

}