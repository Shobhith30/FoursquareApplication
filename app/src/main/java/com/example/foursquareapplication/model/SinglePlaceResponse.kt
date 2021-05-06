package com.example.foursquareapplication.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SinglePlaceResponse(
    private val status: Int,
    private val message: String,
    private val pageNo: Int,
    private val pageSize: Int,
    private val lastPage: Boolean,
    private val data: SinglePlaceData) {

    fun getStatus() = status
    fun getMessage() = message
    fun getPageNo() = pageNo
    fun getPageSize() = pageSize
    fun getLastPage() = lastPage
    fun getData() = data

}

data class SinglePlaceData(
    private val place: Place,
    private val distance: Double
) {

    fun getPlace() = place
    fun getDistance() = distance

}





