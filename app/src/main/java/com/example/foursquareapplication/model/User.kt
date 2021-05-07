package com.example.foursquareapplication.model

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("status") private val status : Int,
    @SerializedName("error") val error : String,
    @SerializedName("message") private  val message : String,
    @SerializedName("pageNo") val pageNo : Int,
    @SerializedName("pageSize") val pageSize : Int,
    @SerializedName("lastPage") val lastPage : Boolean,
    @SerializedName("data") private val data : Data) {

    fun getStatus() = status
    fun getMessage() = message
    fun getData() = data
}

data class Data(
    private val token : String,
    private val userData: UserData) {

    fun getToken() = token
    fun getUserData() = userData

}

data class UserData(
    @SerializedName("id")
    private val userId : Int,
    @SerializedName("username")
    private val userName : String,
    private val image : String,
    private val email :String,
    @SerializedName("date_of_birth")
    private val dateOfBirth : String,
    private val gender : String,
    private val phone : Long) {

    fun getUserId() = userId
    fun getUserName() = userName
    fun getImage() = image
    fun getEmail() = email
    fun getDateOfBirth() = dateOfBirth
    fun getGender() = gender
    fun getPhone() = phone

}
