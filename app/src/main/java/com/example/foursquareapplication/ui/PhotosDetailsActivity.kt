package com.example.foursquareapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ActivityPhotosDetailsBinding
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.viewmodel.PhotosViewModel

class PhotosDetailsActivity : AppCompatActivity() {
    private lateinit var activityPhotoDetailsBinding: ActivityPhotosDetailsBinding
    private lateinit var photosViewModel : PhotosViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_photos_details)

        activityPhotoDetailsBinding = ActivityPhotosDetailsBinding.inflate(layoutInflater)
        setContentView(activityPhotoDetailsBinding.root)

        photosViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(PhotosViewModel::class.java)


        setToolbar()

        val sharedPreferences = getSharedPreferences(
                Constants.USER_PREFERENCE,
                MODE_PRIVATE
        )
        val token = sharedPreferences.getString(Constants.USER_TOKEN,"")
        if (token != null) {
            val newtoken = "Bearer $token"

            val intent = intent
            val photoId = intent.getIntExtra("photoId", 0)
            println("jscj0"+photoId)
            photosViewModel.getPicture(newtoken,photoId)?.observe(this, {
               Glide.with(applicationContext).load(it.getData().getphotoUrl()).into(activityPhotoDetailsBinding.imageview)
                Glide.with(applicationContext).load(it.getData().getuserImage()).into(activityPhotoDetailsBinding.profilePicture)
                activityPhotoDetailsBinding.UserName.text=it.getData().getuserNamw()
                activityPhotoDetailsBinding.date.text=it.getData().getdate()
                activityPhotoDetailsBinding.toolbarTitle.text=it.getData().getplaceName()

            })
        }



    }
    private fun setToolbar() {
        activityPhotoDetailsBinding.toolbar.setNavigationIcon(R.drawable.close_icon)
    //    activityPhotoDetailsBinding.toolbarTitle.text = "Attil"
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