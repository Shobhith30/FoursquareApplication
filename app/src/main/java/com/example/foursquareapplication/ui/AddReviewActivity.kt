package com.example.foursquareapplication.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquareapplication.adapter.AddReviewPhotoAdapter
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ActivityAddReviewBinding
import com.example.foursquareapplication.getFileName
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.Photos
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
import retrofit2.http.Multipart
import java.io.*


private const val REQUEST_CODE=100
class AddReviewActivity : AppCompatActivity() {

    private lateinit var addReviewBinding: ActivityAddReviewBinding
    private lateinit var addReviewViewModel : AddReviewViewModel
    private var selectedImage: Uri?=null
    private val PhotosApi= FourSquareApiInstance.getApiInstance(com.example.foursquareapplication.network.PhotosApi::class.java)




    var modelList=ArrayList<ReviewPhotos>()
    var adapter: AddReviewPhotoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        addReviewBinding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(addReviewBinding.root)

        addReviewViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(AddReviewViewModel::class.java)

        addReviewSubmit()
        setToolbar()


        addReviewBinding.addPhotosToReview.setOnClickListener{
            openImageChooser()
        }


    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type="image/*"
            val minType= arrayOf("image/jpeg","image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES,minType)
            startActivityForResult(it, REQUEST_CODE)
        }    }

    private fun addReviewSubmit() {

        val sharedPreferences = getSharedPreferences(
            Constants.USER_PREFERENCE,
            MODE_PRIVATE
        )
        val token = sharedPreferences.getString(Constants.USER_TOKEN,"")


        addReviewBinding.submit.setOnClickListener{
            val review=addReviewBinding.addReview.text.toString().trim()
            if (review.isEmpty()){
                startActivity(Intent(this, DetailsActivity::class.java))
            }
            else {

                if (token != null) {
                    val newtoken = "Bearer $token"

                val userReview = hashMapOf("userId" to "74","placeId" to "10","review" to review)

                    val parcelFileDiscriptor=contentResolver.openFileDescriptor(selectedImage!!,"r",null)?: return@setOnClickListener
                    val inputStream =FileInputStream(parcelFileDiscriptor.fileDescriptor)
                    val file = File(cacheDir,contentResolver.getFileName(selectedImage!!))
                    val outputStream=FileOutputStream(file)
                    inputStream.copyTo(outputStream)

                    val imagefile=RequestBody.create(MediaType.parse("image/*"),file)

                    PhotosApi.uploadReviewImage(10,74,newtoken,MultipartBody.Part.createFormData("files",file.name,imagefile)
                    ).enqueue(object: Callback<User>{
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            Toast.makeText(applicationContext,"done",Toast.LENGTH_LONG).show()
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Toast.makeText(applicationContext," not done",Toast.LENGTH_LONG).show()
                        }

                    })

                addReviewViewModel.addReview(newtoken,userReview).observe(this, {
                    if (it != null) {
                        println(it)
                        if (it.getStatus() == Constants.STATUS_OK) {

                            Toast.makeText(this,"Review Added",Toast.LENGTH_LONG).show()

                        } else {
                            Toast.makeText(applicationContext, it.getMessage(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    else{

                    }
                })
            }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode==Activity.RESULT_OK){
        when (requestCode) {
            100 -> {
                selectedImage = data?.data
                modelList.add(ReviewPhotos(selectedImage!!))
                initialise(modelList)
            }
        }
    }else{
            super.onActivityResult(requestCode, resultCode, data)

        }

    }

        private fun initialise(capturedPhoto: ArrayList<ReviewPhotos>) {

            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView2)
            val layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
            recyclerView.layoutManager = layoutManager
            adapter?.notifyDataSetChanged()
            adapter = AddReviewPhotoAdapter(capturedPhoto)
            recyclerView.setAdapter(adapter)

    }

    private fun setToolbar() {
        addReviewBinding.toolbar.setNavigationIcon(R.drawable.back_icon)
        addReviewBinding.toolbarTitle.text = "Add Review"

    }


}