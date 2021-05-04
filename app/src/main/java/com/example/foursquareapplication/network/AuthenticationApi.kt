package com.example.foursquareapplication.network

import com.example.foursquareapplication.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthenticationApi {


    @POST("register")
    fun registerUser(@Body user : HashMap<String,String>) : Call<User>

    @POST("authenticate")
    fun authenticateUser(@Body user : HashMap<String,String>) : Call<User>
}