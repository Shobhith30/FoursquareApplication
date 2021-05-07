package com.example.foursquareapplication.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {

    private var locationData : MutableLiveData<Location> = MutableLiveData()

    fun setLocation(location : Location) {
        locationData.postValue( location)
    }
    fun getLocation() : LiveData<Location> =  locationData
}