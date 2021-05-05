package com.example.foursquareapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foursquareapplication.helper.Constants

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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