package com.example.foursquareapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foursquareapplication.model.User
import com.example.foursquareapplication.repository.MainRepository

class AuthenticationViewModel(application: Application)  :AndroidViewModel(application) {
    private val mainRepository = MainRepository(application)

    fun registerUser(user : HashMap<String,String>): LiveData<User> {

        return mainRepository.registerUser(user)
    }

    fun authenticateUser(user : HashMap<String,String>): LiveData<User> {
        return mainRepository.authenticateUser(user)
    }

}