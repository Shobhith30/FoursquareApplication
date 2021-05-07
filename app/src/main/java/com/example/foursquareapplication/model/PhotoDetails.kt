package com.example.foursquareapplication.model

import com.google.gson.annotations.SerializedName

class PhotoDetails (
    private val status: Int,
    private val error:String,
    private val message: String,
    private val pageNo: Int,
    private val pageSize: Int,
    private val lastPage: Boolean,
    private val data: PhotoDetailsData ) {

        fun getStatus() = status
        fun getError()=error
        fun getMessage() = message
        fun getPageNo() = pageNo
        fun getPageSize() = pageSize
        fun getLastPage() = lastPage
        fun getData() = data

}
data class PhotoDetailsData(

        @SerializedName("photoId") val photoId : Int,
        @SerializedName("placeName") val placeName : String,
        @SerializedName("userName") val userName : String,
        @SerializedName("userImage") val userImage : String,
        @SerializedName("date") val date : String,
        @SerializedName("photoUrl") val photoUrl: String){

    fun getphotoId() = photoId
    fun getplaceName() = placeName
    fun getuserNamw() = userName
    fun getuserImage() = userImage
    fun getdate() = date
    fun getphotoUrl()=photoUrl

}

/*
{
    "status": 200,
    "error": "",
    "message": "Success ",
    "pageNo": 0,
    "pageSize": 0,
    "lastPage": false,
    "data": {
    "photoId": 29,
    "placeName": "Gokul Krishna",
    "userName": "shobi@gmail.com",
    "userImage": "https://aws-foursquare.s3.us-east-2.amazonaws.com/UserImage/default.png",
    "date": "06-05-2021",
    "photoUrl": "https://aws-foursquare.s3.us-east-2.amazonaws.com/ReviewImage/fried_rice_using_veggies.jpg"
}
}*/
