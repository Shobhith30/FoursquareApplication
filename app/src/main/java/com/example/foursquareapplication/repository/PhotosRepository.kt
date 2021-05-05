package com.example.foursquareapplication.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foursquareapplication.model.Photos
import com.example.foursquareapplication.model.Review
import com.example.foursquareapplication.network.AuthenticationApi
import com.example.foursquareapplication.network.FourSquareApiInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotosRepository(private val application: Application) {

    private val authenticationApi =
        FourSquareApiInstance.getApiInstance(AuthenticationApi::class.java)

    fun getPictures(placeId:Int,pageNo:Int,pageSize:Int): LiveData<Photos> {
        val getFoodPictures: MutableLiveData<Photos> = MutableLiveData()
        val getFoodPicturescall = authenticationApi.getPictures(placeId,pageNo,pageSize)
        getFoodPicturescall.enqueue(object : Callback<Photos> {
            override fun onResponse(call: Call<Photos>, response: Response<Photos>) {
                if (response.isSuccessful) {
                    getFoodPictures.value = response.body()
                } else {
                    Log.d("resposne","${response.body()?.getError()}")
                    Toast.makeText(application,response.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Photos>, t: Throwable) {

                getFoodPictures.value = null
                Toast.makeText(application, t.message, Toast.LENGTH_SHORT).show()

            }

        })
        return getFoodPictures
    }

}


