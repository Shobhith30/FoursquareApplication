package com.example.foursquareapplication.model

import com.google.gson.annotations.SerializedName

class Feedback(
    @SerializedName("status") private val status : Int,
    @SerializedName("error") val error : String,
    @SerializedName("message") private  val message : String,
    @SerializedName("pageNo") val pageNo : Int,
    @SerializedName("pageSize") val pageSize : Int,
    @SerializedName("lastPage") val lastPage : Boolean,
    @SerializedName("data") private val data : Any) {

    fun getStatus() = status
    fun getMessage() = message
}