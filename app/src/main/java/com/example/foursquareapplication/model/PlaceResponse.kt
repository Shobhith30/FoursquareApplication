package com.example.foursquareapplication.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    private val pageNo : Int,
    private val pageSize : Int,
    private val lastPage : Boolean,
    private val place : List<Place>,
    private val distance : Double)

data class Place(
    @SerializedName("id")
    private val placeId : Int,
    private val name : String,
    private val placeType : List<PlaceType>,
    private val overallRating : Double,
    private val latitude : Double,
    private val longitude : Double,
    private val cost : Int,
    private val phone : Long,
    private val landmark : String,
    private val address : String,
    private val overview : String,
    private val image : String) {

}

data class PlaceType(
    @SerializedName("id")
    private val categoryId : Int,
    @SerializedName("name")
    private val categoryName : String) {

}

