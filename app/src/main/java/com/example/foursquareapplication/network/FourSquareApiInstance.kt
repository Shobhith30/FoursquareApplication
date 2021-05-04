package com.example.foursquareapplication.network

import com.example.foursquareapplication.helper.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FourSquareApiInstance {

    private var instance : Retrofit? = null

    fun <T> getApiInstance(apiClass: Class<T>): T {

        return buildInstance().create(apiClass)
    }

    private fun buildInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(Constants.FOURSQUARE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }

}