package com.example.foursquareapplication.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foursquareapplication.model.PhotoDetails
import com.example.foursquareapplication.model.PhotoDetailsData
import com.example.foursquareapplication.network.FourSquareApiInstance
import com.example.foursquareapplication.network.PhotosApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotosRepository(private val application: Application) {

   /* private val authenticationApi =
        FourSquareApiInstance.getApiInstance(AuthenticationApi::class.java)
*/        private val photosApi= FourSquareApiInstance.getApiInstance(PhotosApi::class.java)


    fun getPhotoDetails(token: String?, photoId: Int): LiveData<PhotoDetails> {
        println("jhc"+photoId)
        val getFoodPictures: MutableLiveData<PhotoDetails> = MutableLiveData()
        val getFoodPicturescall = photosApi.getPhotoDetails(token!!,photoId)
        getFoodPicturescall.enqueue(object : Callback<PhotoDetails> {
            override fun onResponse(call: Call<PhotoDetails>, response: Response<PhotoDetails>) {
                if (response.isSuccessful) {
                    getFoodPictures.value = response.body()
                    println("response"+response.body())
                } else {
                    Log.d("resposne","${response.body()}")
                    println("response data"+response.body())

                    Toast.makeText(application,response.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PhotoDetails>, t: Throwable) {

                Toast.makeText(application, t.message, Toast.LENGTH_SHORT).show()

            }

        })
        return getFoodPictures
    }

}


