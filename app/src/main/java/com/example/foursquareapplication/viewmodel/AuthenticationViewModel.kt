package com.example.foursquareapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foursquareapplication.model.Resource
import com.example.foursquareapplication.model.User
import com.example.foursquareapplication.repository.MainRepository

class AuthenticationViewModel(application: Application)  :AndroidViewModel(application) {
    private val mainRepository = MainRepository(application)

    fun registerUser(user : HashMap<String,String>): LiveData<User> {

        return mainRepository.registerUser(user)
    }

    fun authenticateUser(user : HashMap<String,String>): LiveData<Resource<User>> {
        return mainRepository.authenticateUser(user)
    }

    fun generateOtp(email : HashMap<String,String>): LiveData<Resource<User>> {
        return mainRepository.generateOtp(email)
    }

    fun validateOtp( otp : HashMap<String,String>) : LiveData<Resource<User>>{
        return mainRepository.validateOtp(otp)
    }

    fun confirmPassword( password : HashMap<String,String>) : LiveData<Resource<User>>{
        return  mainRepository.confirmPassword(password)
    }
}