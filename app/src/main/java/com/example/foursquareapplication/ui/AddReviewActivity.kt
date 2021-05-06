package com.example.foursquareapplication.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquareapplication.adapter.AddReviewPhotoAdapter
import com.example.foursquareapplication.model.Model
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ActivityAddReviewBinding
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.viewmodel.AddReviewViewModel
import com.example.foursquareapplication.viewmodel.AuthenticationViewModel


private const val REQUEST_CODE=42
class AddReviewActivity : AppCompatActivity() {

    private lateinit var addReviewBinding: ActivityAddReviewBinding
    private lateinit var addReviewViewModel : AddReviewViewModel

    var modelList=ArrayList<Model>()
    var adapter: AddReviewPhotoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        addReviewBinding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(addReviewBinding.root)


        addReviewViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(AddReviewViewModel::class.java)

addReviewSubmit()
        setToolbar()


        addReviewBinding.addPhotosToReview.setOnClickListener{
            val takePictureIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(this.packageManager)!=null){
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            }
            else{
                Toast.makeText(this,"unable to open camera", Toast.LENGTH_LONG).show()
            }
        }
        /*addReviewBinding.submit.setOnClickListener{
            startActivity(Intent(this, DetailsActivity::class.java))
        }*/

    }

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

        if (requestCode== REQUEST_CODE && resultCode== Activity.RESULT_OK){
             val takenImage= data?.extras?.get("data") as Bitmap

            modelList.add(Model(takenImage))

            initialise(modelList)


        }
        else{
            super.onActivityResult(requestCode, resultCode, data)

        }

    }

        private fun initialise(capturedPhoto: ArrayList<Model>) {

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