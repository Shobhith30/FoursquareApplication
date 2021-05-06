package com.example.foursquareapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foursquareapplication.model.PhotoModel
import com.example.foursquareapplication.R
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


        
       /* var modelItems: PhotoModel =intent.getSerializableExtra("data") as PhotoModel

        activityPhotoDetailsBinding.imageview.setImageResource(modelItems.image!!)*/

        activityPhotoDetailsBinding.toolbar.setNavigationIcon(R.drawable.close_icon)
        activityPhotoDetailsBinding.toolbarTitle.text = "Attil"
        activityPhotoDetailsBinding.toolbar.inflateMenu(R.menu.menu_share)
        activityPhotoDetailsBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        activityPhotoDetailsBinding.toolbar.setOnMenuItemClickListener {
            sharePhoto()
            true
        }

    }
    private fun sharePhoto() {
        val sharePlaceIntent = Intent(Intent.ACTION_SEND)
        sharePlaceIntent.putExtra(Intent.EXTRA_TEXT,"photo")
        sharePlaceIntent.type = "plain/text"
        val chooser = Intent.createChooser(sharePlaceIntent,"Select App")
        startActivity(chooser)
    }

}