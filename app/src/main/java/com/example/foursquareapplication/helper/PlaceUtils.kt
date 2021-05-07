package com.example.foursquareapplication.helper

import android.content.Context
import com.example.foursquareapplication.R

class PlaceUtils {

    fun getCost(cost : Int?,context : Context): String? {
        return when(cost){
            1-> context.getString(R.string.rupees_1)
            2-> context.getString(R.string.rupees_2)
            3-> context.getString(R.string.rupees_3)
            4-> context.getString(R.string.rupees_4)
            else -> null
        }
    }

    fun getShortPlaceType(type : String) : String?{
        val index = type.lastIndexOf("Restaurant")
        var restaurantType : String? = null
        if(index!=-1){
            restaurantType = type.substring(0,index)
        }
        return restaurantType

    }
}