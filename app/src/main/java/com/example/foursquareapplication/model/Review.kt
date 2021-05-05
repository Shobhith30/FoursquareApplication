package com.example.foursquareapplication.model

data class Review (
    private val status: Int,
    private val message: String,
    private val pageNo: Int,
    private val pageSize: Int,
    private val lastPage: Boolean,
    private val data: ArrayList<ReviewData>) {

        fun getStatus() = status
        fun getMessage() = message
        fun getPageNo() = pageNo
        fun getPageSize() = pageSize
        fun getLastPage() = lastPage
        fun getData() = data
    }

    data class ReviewData(
        private val placeId : Int,
        private val placeName : Int,
        private val userImage : String,
        private val userName : String,
        private val review : String,
        private val date : String ) {

        fun getPlaceId() = placeId
        fun getPlaceName() = placeName
        fun getUserImage() = userImage
        fun getReview() = review
        fun getDate() = date

    }
