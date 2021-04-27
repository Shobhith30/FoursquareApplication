package com.example.foursquareapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.graphics.toColor
import com.example.foursquareapplication.databinding.ActivityPhotosDetailsBinding

class PhotosDetailsActivity : AppCompatActivity() {
    private lateinit var activityPhotoDetailsBinding: ActivityPhotosDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      /*  requestWindowFeature(1)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor=Color.TRANSPARENT
        }*/
        setContentView(R.layout.activity_photos_details)



        activityPhotoDetailsBinding = ActivityPhotosDetailsBinding.inflate(layoutInflater)
        setContentView(activityPhotoDetailsBinding.root)
        
        var modelItems:PhotoModel=intent.getSerializableExtra("data") as PhotoModel

        activityPhotoDetailsBinding.imageview.setImageResource(modelItems.image!!)

        activityPhotoDetailsBinding.toolbar.setNavigationIcon(R.drawable.close_icon)
        activityPhotoDetailsBinding.toolbarTitle.text = "Attil"
        activityPhotoDetailsBinding.toolbar.inflateMenu(R.menu.menu_share)


    }

}