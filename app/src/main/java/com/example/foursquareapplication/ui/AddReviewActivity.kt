package com.example.foursquareapplication.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquareapplication.R
import com.example.foursquareapplication.adapter.AddReviewPhotoAdapter
import com.example.foursquareapplication.databinding.ActivityAddReviewBinding
import com.example.foursquareapplication.getFileName
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.ApiResponse
import com.example.foursquareapplication.model.Review
import com.example.foursquareapplication.model.ReviewPhotos
import com.example.foursquareapplication.model.User
import com.example.foursquareapplication.network.FourSquareApiInstance
import com.example.foursquareapplication.viewmodel.AddReviewViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


private const val REQUEST_CODE=100
class AddReviewActivity : AppCompatActivity() {

    private lateinit var addReviewBinding: ActivityAddReviewBinding
    private lateinit var addReviewViewModel : AddReviewViewModel
    private var selectedImage: Uri?=null
    private val PhotosApi= FourSquareApiInstance.getApiInstance(com.example.foursquareapplication.network.PhotosApi::class.java)
    private lateinit var sharedPreferences: SharedPreferences


    var modelList=ArrayList<ReviewPhotos>()
    var adapter: AddReviewPhotoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        addReviewBinding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(addReviewBinding.root)

        sharedPreferences  = getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE)
        addReviewViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            AddReviewViewModel::class.java
        )
        addReviewSubmit()
        setToolbar()


        addReviewBinding.addPhotosToReview.setOnClickListener{
            openImageChooser()
        }


    }

    private fun openImageChooser() {
        val permission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.REQUEST_EXTERNAL_STORAGE
            )
        } else {
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                val minType = arrayOf("image/jpeg", "image/png")
                it.putExtra(Intent.EXTRA_MIME_TYPES, minType)
                it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                startActivityForResult(it, REQUEST_CODE)
            }
        }
    }

    private fun addReviewSubmit() {

        val sharedPreferences = getSharedPreferences(
            Constants.USER_PREFERENCE,
            MODE_PRIVATE
        )
        val token = sharedPreferences.getString(Constants.USER_TOKEN, "")
        val userId = sharedPreferences.getString(Constants.USER_ID,"").toString()
        val placeId = intent.extras?.getInt(Constants.PLACE_ID).toString()


        addReviewBinding.submit.setOnClickListener{
            val review=addReviewBinding.addReview.text.toString().trim()
            if (review.isEmpty() && modelList.isEmpty()){
                startActivity(Intent(this, DetailsActivity::class.java))
            }
            else {

                if (token != null) {
                    val newtoken = "Bearer $token"

                    val userReview = hashMapOf(
                        "userId" to userId,
                        "placeId" to placeId,
                        "review" to review
                    )

                addReviewViewModel.addReview(newtoken, userReview).observe(this, {
                    if (it != null) {
                        println(it)
                        if (it.getStatus() == Constants.STATUS_OK) {
                            if (modelList.isNotEmpty()){
                                addReviewImage()
                            }else{
                                Toast.makeText(applicationContext,"Review Added",Toast.LENGTH_LONG).show()
                                onBackPressed()
                            }
                        } else {
                            Toast.makeText(applicationContext, it.getMessage(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })
            }
            }

        }
    }
    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == Constants.REQUEST_EXTERNAL_STORAGE)
                openImageChooser()
        }
    }
    private fun addReviewImage() {
        val reviewImagesParts = arrayListOf<MultipartBody.Part>()
        val sharedPreferences = getSharedPreferences(
            Constants.USER_PREFERENCE,
            MODE_PRIVATE
        )
        var token = sharedPreferences.getString(Constants.USER_TOKEN, "")
        val placeId = intent.extras?.getInt(Constants.PLACE_ID)
        val userId = sharedPreferences.getString(Constants.USER_ID,"")

        if(token!=null && placeId!=null && userId!=null) {
            token = "Bearer $token"

            for (image in modelList) {
                image.image?.let {
                    val file = File(getRealPathFromURI(it))
                    val reviewBody = RequestBody.create(MediaType.parse("image/*"), file)
                    val part = MultipartBody.Part.createFormData(
                        "files",
                        file.name,
                        reviewBody
                    )
                    reviewImagesParts.add(part)

                }


            }
            Log.d("Images",reviewImagesParts.size.toString())
            if (reviewImagesParts.size > 0) {
                Toast.makeText(applicationContext, "Uploading Images..Please wait..", Toast.LENGTH_SHORT).show()

                PhotosApi.uploadReviewImage(
                    placeId, userId.toInt(), token, reviewImagesParts
                ).enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                        if (response.isSuccessful) {
                            if (response.body()?.getStatus() == Constants.STATUS_OK) {
                                Toast.makeText(
                                    applicationContext,
                                    "Review Added",
                                    Toast.LENGTH_LONG
                                ).show()
                                onBackPressed()
                            }
                        } else {
                            Toast.makeText(applicationContext, "Error Uploading Image", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                })
            }
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
                    initialise(modelList)
                }

            }
        }

    }

        private fun initialise(capturedPhoto: ArrayList<ReviewPhotos>) {

            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView2)
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.layoutManager = layoutManager
            adapter?.notifyDataSetChanged()
            adapter = AddReviewPhotoAdapter(capturedPhoto)
            recyclerView.setAdapter(adapter)

    }

    private fun setToolbar() {
        addReviewBinding.toolbar.setNavigationIcon(R.drawable.back_icon)
        addReviewBinding.toolbarTitle.text = intent.extras?.getString(Constants.PLACE_NAME)
        addReviewBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }


}