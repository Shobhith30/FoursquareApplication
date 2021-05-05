package com.example.foursquareapplication.model

import com.google.gson.annotations.SerializedName

data class Photos(
    private val status: Int,
    private val error:String,
    private val message: String,
    private val pageNo: Int,
    private val pageSize: Int,
    private val lastPage: Boolean,
    private val data: ArrayList<PhotoData>) {

    fun getStatus() = status
    fun getError()=error
    fun getMessage() = message
    fun getPageNo() = pageNo
    fun getPageSize() = pageSize
    fun getLastPage() = lastPage
    fun getData() = data
}

data class PhotoData(

    @SerializedName("photo_id") val photoId : Int,
    @SerializedName("user_id") val userId : Int,
    @SerializedName("place_id") val placeId : Int,
    @SerializedName("image") val image : String,
    @SerializedName("date") val date : String){


    fun getphotoId() = photoId
    fun getuserId() = userId
    fun getplaceId() = placeId
    fun getImageUrl() = image
    fun getdate() = date

}

/*
{
    "status": 200,
    "error": " ",
    "message": "Success",
    "pageNo": 0,
    "pageSize": 2,
    "lastPage": true,
    "data": [
    {
        "photo_id": 11,
        "user_id": 35,
        "place_id": 10,
        "image": "https://aws-foursquare.s3.us-east-2.amazonaws.com/ReviewImage/mae-mu-noodles-vegetables-egg.jpg",
        "date": "04-05-2021"
    },
    {
        "photo_id": 12,
        "user_id": 35,
        "place_id": 10,
        "image": "https://aws-foursquare.s3.us-east-2.amazonaws.com/ReviewImage/mae-mu-noodles-vegetables-egg.jpg",
        "date": "04-05-2021"
    }
    ]
}
*/

