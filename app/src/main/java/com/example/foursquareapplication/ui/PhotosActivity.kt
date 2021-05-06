package com.example.foursquareapplication.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foursquareapplication.R
import com.example.foursquareapplication.adapter.PictureAdapter
import com.example.foursquareapplication.adapter.ReviewAdapter
import com.example.foursquareapplication.databinding.ActivityPhotosBinding
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.PhotoData
import com.example.foursquareapplication.model.Photos
import com.example.foursquareapplication.viewmodel.PhotosViewModel
import com.example.foursquareapplication.viewmodel.ReviewViewModel


private const val REQUEST_CODE=42
class PhotosActivity : AppCompatActivity() {

    private lateinit var photoBinding: ActivityPhotosBinding
    private lateinit var pictureAdapter : PictureAdapter
    private lateinit var photosViewModel : PhotosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoBinding = ActivityPhotosBinding.inflate(layoutInflater)
        setContentView(photoBinding.root)

        setToolbar()

        photosViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(PhotosViewModel::class.java)
        pictureAdapter= PictureAdapter(this)
        val gridView=GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false)
        photoBinding.gridViewRecyclerView.layoutManager=gridView
        photoBinding.gridViewRecyclerView.adapter=pictureAdapter

      //  val placeId = intent.getIntExtra(Constants.PLACE_ID,0)
        val placeId=10
        if(placeId!=0) {
            photosViewModel.getPictures(placeId)?.observe(this,{
                if (it!=null)
                    pictureAdapter.submitList(it)
            })

        }
        else{
            println("124578"+placeId)
        }

    }



    private fun setToolbar() {

        photoBinding.toolbar.setNavigationIcon(R.drawable.back_icon)
        photoBinding.toolbarTitle.text = "Attil"
        photoBinding.toolbar.inflateMenu(R.menu.menu_photo)
        photoBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        photoBinding.toolbar.setOnMenuItemClickListener{
            addPhotos()
            true
        }
    }
    private fun addPhotos(){

        val takePictureIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(this.packageManager)!=null){
            startActivityForResult(takePictureIntent, REQUEST_CODE)
        }
        else{
            Toast.makeText(this,"Unable to open camera", Toast.LENGTH_LONG).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode== REQUEST_CODE && resultCode== Activity.RESULT_OK){
            val takenImage= data?.extras?.get("data") as Bitmap

        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

}

