package com.example.foursquareapplication.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foursquareapplication.R
import com.example.foursquareapplication.adapter.PictureAdapter
import com.example.foursquareapplication.databinding.ActivityPhotosBinding
import com.example.foursquareapplication.getFileName
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.ReviewPhotos
import com.example.foursquareapplication.model.User
import com.example.foursquareapplication.network.FourSquareApiInstance
import com.example.foursquareapplication.viewmodel.PhotosViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


private const val REQUEST_CODE=100
class PhotosActivity : AppCompatActivity() {

    private lateinit var photoBinding: ActivityPhotosBinding
    private lateinit var pictureAdapter : PictureAdapter
    private lateinit var photosViewModel : PhotosViewModel
    private var selectedImage: Uri?=null
    private val PhotosApi= FourSquareApiInstance.getApiInstance(com.example.foursquareapplication.network.PhotosApi::class.java)
    var modelList=ArrayList<ReviewPhotos>()

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
            openImageChooser()
                       true
        }
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type="image/*"
            val minType= arrayOf("image/jpeg","image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES,minType)
            it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            startActivityForResult(Intent.createChooser(it, "SELECT IMAGES"), REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== REQUEST_CODE){
            if (resultCode==Activity.RESULT_OK){
                if (data!!.clipData!=null){
                    val count=data.clipData!!.itemCount
                    for (i in 0 until count){
                        selectedImage=data.clipData!!.getItemAt(i).uri
                        modelList.add(ReviewPhotos(selectedImage!!))
                    }
                }
                else{
                    val selectedImage=data.data
                    modelList.add(ReviewPhotos(selectedImage!!))


                }
                println("size"+modelList.size)


            }
        }
        uploadImage()

    }

    private fun uploadImage(){
        val sharedPreferences = getSharedPreferences(
                Constants.USER_PREFERENCE,
                MODE_PRIVATE
        )
        val token = sharedPreferences.getString(Constants.USER_TOKEN,"")
        println(token)
        if (token != null) {
            val newtoken = "Bearer $token"

            for (images in modelList) {

                println("sizeM"+modelList.size)

                val parcelFileDiscriptor = contentResolver.openFileDescriptor(selectedImage!!, "r", null)
                        ?: return
                val inputStream = FileInputStream(parcelFileDiscriptor.fileDescriptor)
                val file = File(cacheDir, contentResolver.getFileName(images.image!!))
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)

                val imagefile = RequestBody.create(MediaType.parse("image/*"), file)

//                PhotosApi.uploadReviewImage(9, 74, newtoken, MultipartBody.Part.createFormData("files", file.name, imagefile)
//                ).enqueue(object : Callback<User> {
//                    override fun onResponse(call: Call<User>, response: Response<User>) {
//                        Toast.makeText(applicationContext,"uploaded",Toast.LENGTH_LONG).show()
//                    }
//
//                    override fun onFailure(call: Call<User>, t: Throwable) {
//                        println("error"+t.message)
//                        Toast.makeText(applicationContext,"not uploaded"+ t.message,Toast.LENGTH_LONG).show()
//
//                    }
//
//                })
            }
            startActivity(Intent(this,PhotosActivity::class.java))

        }
    }

}

