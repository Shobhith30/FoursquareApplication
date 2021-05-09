package com.example.foursquareapplication.helper

import android.graphics.Color

class ChangeRatingColor {
    fun getRatingColor(rating : Float) : Int{
        return when{
            (rating<=2.5) -> Color.parseColor("#FF0000")
            rating>2.5 && rating<= 5.0 -> Color.parseColor("#FFD700")
            rating>5.0 && rating<=7.5 -> Color.parseColor("#90EE90")
            else -> Color.parseColor("#36B000")
        }
    }
}