package com.example.foursquareapplication.model

data class AddFavouriteResponse (
    private val status: Int,
    private val message: String,
    private val pageNo: Int,
    private val pageSize: Int,
    private val lastPage: Boolean,
    private val data: Any
){
    fun getStatus() = status
    fun getMessage() = message
    fun getPageNo() = pageNo
    fun getPageSize() = pageSize
    fun getLastPage() = lastPage
    fun getData() = data
}