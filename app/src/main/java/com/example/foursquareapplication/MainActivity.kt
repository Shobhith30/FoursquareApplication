package com.example.foursquareapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.Place
import com.example.foursquareapplication.model.PlaceResponse
import com.example.foursquareapplication.model.PlaceType
import com.example.foursquareapplication.ui.DetailsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this,DetailsActivity::class.java)
        val budnle = Bundle()

        val plac = Place(11,"Gufha Resturant", arrayListOf(PlaceType(1,"INDIAN_RESTAURANT")),3.0f,12.316212910073402, 76.65986119781437,6,9976876324,"fff","35/A, Bangalore Nilgiri Rd, Nazarbad, Mysuru, Karnataka 570001","Multi-cuisine menus & cocktails offered in Hotel Pai Vista's dramatic cave-themed dining room","https://aws-foursquare.s3.us-east-2.amazonaws.com/PlaceImage/GufhaRestaurant.jpg")
        val dataPlace = arrayListOf(DataPlace(plac,2.0))
        val locationResponse =  PlaceResponse(200,"ok",10,1,true,dataPlace)
        budnle.putParcelable(Constants.PLACE_RESPOSNE,locationResponse)
        intent.putExtras(budnle)
        startActivity(intent)
        val sharedPreferences = getSharedPreferences(
            Constants.USER_PREFERENCE,
            MODE_PRIVATE
        )
        val token = sharedPreferences.getString(Constants.USER_TOKEN,"")
        if(token!=null){
            val newtoken = "Bearer $token"
        }

    }
}