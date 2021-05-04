package com.example.foursquareapplication.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foursquareapplication.model.User
import com.example.foursquareapplication.network.AuthenticationApi
import com.example.foursquareapplication.network.FourSquareApiInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository(private val application: Application) {
    private val authenticationApi =
        FourSquareApiInstance.getApiInstance(AuthenticationApi::class.java)


    fun registerUser(user: HashMap<String, String>): LiveData<User> {
        val registerUser: MutableLiveData<User> = MutableLiveData()
        val registerDetails = authenticationApi.registerUser(user)
        registerDetails.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d("uservalue", response.body().toString())
                if (response.isSuccessful) {
                    registerUser.value = response.body()
                } else {
                    Toast.makeText(application, response.raw().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                registerUser.value = (null)
                Toast.makeText(application, t.message, Toast.LENGTH_SHORT).show()
            }

        })
        return registerUser
    }

    fun authenticateUser(user: HashMap<String, String>): LiveData<User> {
        val loginUser: MutableLiveData<User> = MutableLiveData()
        val authenticateCall = authenticationApi.authenticateUser(user)
        authenticateCall.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    loginUser.value = response.body()
                } else {
                    Toast.makeText(application,response.raw().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {

                loginUser.value = null
                Toast.makeText(application, t.message, Toast.LENGTH_SHORT).show()

            }

        })
        return loginUser
    }

}