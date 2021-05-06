package com.example.foursquareapplication.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.Review
import com.example.foursquareapplication.model.User
import com.example.foursquareapplication.network.AuthenticationApi
import com.example.foursquareapplication.network.FourSquareApiInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewRepository(private val application: Application)  {
    private val authenticationApi =
        FourSquareApiInstance.getApiInstance(AuthenticationApi::class.java)

    fun addUserReview(token:String,user: HashMap<String, String>): LiveData<Review> {
        val addReviewUser: MutableLiveData<Review> = MutableLiveData()
        val addReviewUserCall = authenticationApi.addReview(token,user)
        addReviewUserCall.enqueue(object : Callback<Review> {
            override fun onResponse(call: Call<Review>, response: Response<Review>) {
                if (response.isSuccessful) {
                    addReviewUser.value = response.body()
                } else {
                    Log.d("resposne","${response.body()}")
                    Toast.makeText(application,response.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Review>, t: Throwable) {

                addReviewUser.value = null
                Toast.makeText(application, t.message, Toast.LENGTH_SHORT).show()

            }

        })
        return addReviewUser
    }



}