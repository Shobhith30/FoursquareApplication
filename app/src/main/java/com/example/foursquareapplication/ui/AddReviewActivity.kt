package com.example.foursquareapplication.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquareapplication.adapter.AddReviewPhotoAdapter
import com.example.foursquareapplication.model.Model
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ActivityAddReviewBinding


private const val REQUEST_CODE=42
class AddReviewActivity : AppCompatActivity() {

    private lateinit var addReviewBinding: ActivityAddReviewBinding
    var modelList=ArrayList<Model>()
    var adapter: AddReviewPhotoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        addReviewBinding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(addReviewBinding.root)

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
        addReviewBinding.submit.setOnClickListener{
            startActivity(Intent(this, DetailsActivity::class.java))
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