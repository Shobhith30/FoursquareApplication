package com.example.foursquareapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.graphics.toColor
import com.example.foursquareapplication.databinding.ActivityPhotosDetailsBinding

class PhotosDetailsActivity : AppCompatActivity() {
    private lateinit var feedBackBinding: ActivityPhotosDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      /*  requestWindowFeature(1)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor=Color.TRANSPARENT
        }*/
        setContentView(R.layout.activity_photos_details)



        feedBackBinding = ActivityPhotosDetailsBinding.inflate(layoutInflater)
        setContentView(feedBackBinding.root)

        var modelItems:PhotoModel=intent.getSerializableExtra("data") as PhotoModel

        feedBackBinding.imageview.setImageResource(modelItems.image!!)

        feedBackBinding.toolbar.setNavigationIcon(R.drawable.close_icon)
        feedBackBinding.toolbarTitle.text = "Attil"
        feedBackBinding.toolbar.inflateMenu(R.menu.menu_share)


    }

}