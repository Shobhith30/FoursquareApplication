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
import com.bumptech.glide.Glide
import com.example.foursquareapplication.model.PhotoModel
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ActivityPhotosBinding
import com.example.foursquareapplication.model.Photos
import com.example.foursquareapplication.viewmodel.PhotosViewModel


private const val REQUEST_CODE=42
class PhotosActivity : AppCompatActivity() {

    private lateinit var photoBinding: ActivityPhotosBinding
    private lateinit var photosViewModel : PhotosViewModel


/*
    var modelist=ArrayList<PhotoModel>()
*/

    var images= intArrayOf(R.drawable.idli_vada,R.drawable.kabab)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoBinding = ActivityPhotosBinding.inflate(layoutInflater)
        setContentView(photoBinding.root)
        photosViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(PhotosViewModel::class.java)


        setToolbar()

        val gridView=findViewById<GridView>(R.id.gridView)

        /*for (i in images.indices){
            modelist.add(PhotoModel(images[i]))
        }*/

/*
        var customAdapter=customAdapter(modelist, this, it)
*/

/*
        gridView.adapter=customAdapter
*/
/*
        gridView.setOnItemClickListener { parent, view, position, id ->
            val intent= Intent(this, PhotosDetailsActivity::class.java)
            intent.putExtra("data",modelist[position])
            startActivity(intent)
        }*/

        photosViewModel.getPhotos(10,0,5).observe(this,{
            println(it)

            var customAdapter=customAdapter(this,it)
            gridView.adapter= customAdapter

            gridView.setOnItemClickListener { parent, view, position, id ->
                val intent= Intent(this, PhotosDetailsActivity::class.java)

/*
                intent.putExtra("data",modelist[position])
*/
                startActivity(intent)
            }


        })

        getPictures()

    }

    private fun getPictures() {

       /* var imageView=findViewById<ImageView>(R.id.image1)


        photosViewModel.addReview(10,0,5).observe(this,{
            println(it)

            Glide.with(applicationContext).load(it.getData()[0].getImageUrl()).into(imageView)

        })*/
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


    class customAdapter(
/*
        var itemModel: ArrayList<PhotoModel>,
*/
        var context: Context,
        var photos: Photos
    ): BaseAdapter(){

        var layoutInflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getCount(): Int {
            return photos.getPageSize()
        }

        override fun getItem(position: Int): Any {
            return photos.getData()[position]
/*
            return itemModel[position]
*/
        }

        override fun getItemId(position: Int): Long {

            return position.toLong()
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var view=convertView
            if (view==null){
                view=layoutInflater.inflate(R.layout.item_photos,parent,false)

            }

            var imageView=view?.findViewById<ImageView>(R.id.image1)


            Glide.with(context).load(photos.getData()[0].getImageUrl()).into(imageView!!)

            println(photos.getData()[0].getImageUrl())

           /* photosViewModel.addReview(10,0,5).observe(this,{
                println(it)

                if (imageView != null) {
                    Glide.with(context).load(it.getData()[0].getImageUrl()).into(imageView)
                }

            })*/

/*
            Glide.with(context).load("https://aws-foursquare.s3.us-east-2.amazonaws.com/ReviewImage/mae-mu-noodles-vegetables-egg.jpg").into(imageView!!)
*/
/*
            imageView?.setImageResource(itemModel[position].image!!)
*/


            return view!!

        }
    }
}

