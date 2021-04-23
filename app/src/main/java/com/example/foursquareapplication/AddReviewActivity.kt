package com.example.foursquareapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.ButtonBarLayout
import androidx.appcompat.widget.Toolbar
import com.example.foursquareapplication.databinding.ActivityAddReviewBinding

class AddReviewActivity : AppCompatActivity() {

    private lateinit var addReviewBinding: ActivityAddReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        addReviewBinding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(addReviewBinding.root)

        setToolbar()

        val submit=findViewById<Button>(R.id.submit)

    }

    private fun setToolbar() {
        addReviewBinding.toolbar.setNavigationIcon(R.drawable.back_icon)
        addReviewBinding.toolbarTitle.text = "Add Review"
        


    }
}