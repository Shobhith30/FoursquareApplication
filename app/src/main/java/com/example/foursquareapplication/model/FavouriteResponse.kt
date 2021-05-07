package com.example.foursquareapplication.model

import android.os.Parcel
import android.os.Parcelable

data class FavouriteResponse (
    private val status: Int,
    private val message: String,
    private val pageNo: Int,
    private val pageSize: Int,
    private val lastPage: Boolean,
    private val data: List<Place>
    ){
        fun getStatus() = status
        fun getMessage() = message
        fun getPageNo() = pageNo
        fun getPageSize() = pageSize
        fun getLastPage() = lastPage
        fun getData() = data
}