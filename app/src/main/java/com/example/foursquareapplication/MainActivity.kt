package com.example.foursquareapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //supportFragmentManager.beginTransaction().add(R.id.main_root,SearchFilterFragment()).commit()
        startActivity(Intent(this,DetailsActivity::class.java))

    }
}