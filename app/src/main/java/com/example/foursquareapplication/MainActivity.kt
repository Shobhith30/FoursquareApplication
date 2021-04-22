package com.example.foursquareapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val inflater = layoutInflater.inflate(R.layout.rating_dialog,findViewById(R.id.main_root),false)
        val dialog= AlertDialog.Builder(this).setView(inflater).create()
        dialog.getWindow()?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }
}