package com.example.foursquareapplication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foursquareapplication.R

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val getIn = findViewById<TextView>(R.id.get_in_button)
        getIn.setOnClickListener {
            val intent = Intent(this, ConfirmPassword::class.java)
            startActivity(intent)
        }
    }
}