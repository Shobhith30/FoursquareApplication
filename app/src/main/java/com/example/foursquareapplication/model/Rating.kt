package com.example.foursquareapplication.model

import com.google.gson.annotations.SerializedName

class Rating(
@SerializedName("status") private val status : Int,
@SerializedName("error") private val error : String,
@SerializedName("message") private  val message : String,
@SerializedName("pageNo") private val pageNo : Int,
@SerializedName("pageSize") private val pageSize : Int,
@SerializedName("lastPage") private val lastPage : Boolean,
@SerializedName("data") private val data : Any) {

    fun getStatus() = status
    fun getMessage() = message
    fun getData() = data
}