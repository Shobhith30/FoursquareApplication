package com.example.foursquareapplication.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foursquareapplication.model.FavouriteResponse

import com.example.foursquareapplication.network.FavouriteApi
import com.example.foursquareapplication.network.FourSquareApiInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavouriteRepository (private val application: Application)  {
    private val favouriteApi =
        FourSquareApiInstance.getApiInstance(FavouriteApi::class.java)

    fun getFavourite(userId: Int, pageNumber: Int, pageSize: Int, token: String): LiveData<FavouriteResponse> {
        val favouritePlace : MutableLiveData<FavouriteResponse> = MutableLiveData()
        val getFavouriteCall = favouriteApi.getFavourite(userId,pageNumber,pageSize,token)
        getFavouriteCall.enqueue(object : Callback<FavouriteResponse> {
            override fun onResponse(call: Call<FavouriteResponse>, response: Response<FavouriteResponse>) {
                if (response.isSuccessful) {
                    favouritePlace.value = response.body()
                } else {
                    Log.d("response","${response.body()}")
                    Toast.makeText(application,response.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FavouriteResponse>, t: Throwable) {

                favouritePlace.value = null
                Toast.makeText(application, t.message, Toast.LENGTH_SHORT).show()

            }

        })
        return favouritePlace
    }

}