package com.example.foursquareapplication.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ActivityPhotosDetailsBinding
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.viewmodel.PhotosViewModel
import java.io.ByteArrayOutputStream
import java.util.jar.Manifest


class PhotosDetailsActivity : AppCompatActivity() {
    private lateinit var activityPhotoDetailsBinding: ActivityPhotosDetailsBinding
    private lateinit var photosViewModel : PhotosViewModel

    var image:Bitmap?=null

    val permission= arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)


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
        val token = sharedPreferences.getString(Constants.USER_TOKEN, "")
        if (token != null) {
            val newtoken = "Bearer $token"

            val intent = intent
            val photoId = intent.getIntExtra("photoId", 0)
            println("jscj0" + photoId)
            photosViewModel.getPictureDetails(newtoken, photoId)?.observe(this, {
                Glide.with(applicationContext).load(it.getData().getphotoUrl()).into(activityPhotoDetailsBinding.imageview)
                Glide.with(applicationContext).load(it.getData().getuserImage()).into(activityPhotoDetailsBinding.profilePicture)
                activityPhotoDetailsBinding.UserName.text = it.getData().getuserNamw()
                activityPhotoDetailsBinding.date.text = it.getData().getdate()
                activityPhotoDetailsBinding.toolbarTitle.text = it.getData().getplaceName()




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

            val image:Bitmap?=getBitmapfromView(activityPhotoDetailsBinding.imageview)
            this.image=image
            checkPermissions()

            true
        }
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode==100){
            when{
                grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED
                ->{
                    val share = Intent(Intent.ACTION_SEND)
                    share.type = "image/*"
                    share.putExtra(Intent.EXTRA_STREAM, getImageUri(this, image!!))
                    startActivity(Intent.createChooser(share, "Share via"))
                }
            }
            return
        }
    }


    private fun checkPermissions() {
        var result:Int
        val permissionAdded:MutableList<String> = ArrayList()
        for (p in permission){
            result=ContextCompat.checkSelfPermission(this,p)
            if (result!=PackageManager.PERMISSION_GRANTED){
                permissionAdded.add(p)
            }
        }
        if (permissionAdded.isNotEmpty()){
            ActivityCompat.requestPermissions(this,permissionAdded.toTypedArray(),100)
        }
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/*"
        share.putExtra(Intent.EXTRA_STREAM, getImageUri(this, image!!))
        startActivity(Intent.createChooser(share, "Share via"))

    }

    private fun getBitmapfromView(view: ImageView):Bitmap?{
        val bitmap=Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas=Canvas(bitmap)
        view.draw(canvas)
        return bitmap

    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }



}