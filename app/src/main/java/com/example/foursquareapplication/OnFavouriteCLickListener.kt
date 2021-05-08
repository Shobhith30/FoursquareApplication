package com.example.foursquareapplication

interface OnFavouriteCLickListener {
    fun onFavouriteClick(isFav : Boolean,id:Int?)
    fun removeFavourite(id : Int?){}
}