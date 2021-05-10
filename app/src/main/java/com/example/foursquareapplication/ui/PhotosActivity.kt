package com.example.foursquareapplication.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foursquareapplication.R
import com.example.foursquareapplication.adapter.HeaderAdapter
import com.example.foursquareapplication.adapter.PictureAdapter
import com.example.foursquareapplication.databinding.ActivityPhotosBinding
import com.example.foursquareapplication.getFileName
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.ApiResponse
import com.example.foursquareapplication.model.Review
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
        photoBinding.gridViewRecyclerView.adapter=pictureAdapter.withLoadStateHeaderAndFooter(
            header = HeaderAdapter{pictureAdapter.retry()},
            footer = HeaderAdapter{pictureAdapter.retry()}
        )
        photoBinding.retry.setOnClickListener {
            pictureAdapter.retry()
        }

      val placeId = intent.getIntExtra(Constants.PLACE_ID,0)

        if(placeId!=0) {
            photosViewModel.getPicture(placeId).observe(this){

                    pictureAdapter.submitData(lifecycle,it)
            }

            pictureAdapter.addLoadStateListener { loadState->


                photoBinding.apply {

                    progress.isVisible = loadState.source.refresh is LoadState.Loading
                    gridViewRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                    retry.isVisible = loadState.source.refresh is LoadState.Error
                    errorMessage.isVisible = loadState.source.refresh is LoadState.Error
                    if(loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached
                        && pictureAdapter.itemCount<1){
                        gridViewRecyclerView.visibility  = View.INVISIBLE
                        noData.isVisible = true
                    }else{
                        noData.isVisible = false
                    }

                }
            }

        }

    }



    private fun setToolbar() {

        photoBinding.toolbar.setNavigationIcon(R.drawable.back_icon)
        photoBinding.toolbarTitle.text = intent.extras?.getString(Constants.PLACE_NAME)
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
                addReviewImage()
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
        val userId = sharedPreferences.getString(Constants.USER_ID,"")
        val placeId = intent.extras?.getInt(Constants.PLACE_ID)
        if(token!=null && userId!=null && placeId!=null){
            val userId = userId.toInt()
            token = "Bearer $token"

            for (image in modelList) {
                image.image?.let{
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


            PhotosApi.uploadReviewImage(
                    placeId, userId, token, reviewImagesParts
            ).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()?.getStatus() == Constants.STATUS_OK) {
                            Toast.makeText(applicationContext, "Photo(s) Added", Toast.LENGTH_LONG)
                                .show()

                        } else {

                            Toast.makeText(applicationContext, response.body()?.getMessage(), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Error Uploading Image",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

            })
        }
        modelList.clear()
    }

}

