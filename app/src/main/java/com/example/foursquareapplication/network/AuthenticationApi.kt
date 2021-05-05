package com.example.foursquareapplication.network

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

    @PUT("validateOtp")
    fun validateOtp(@Body otp: HashMap<String, String>) : Call<User>

    @PUT("changePassword")
    fun confirmPassword( @Body password : HashMap<String,String>) : Call<User>
}