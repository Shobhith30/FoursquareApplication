package com.example.foursquareapplication

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
import com.example.foursquareapplication.databinding.ActivityPhotosBinding


private const val REQUEST_CODE=42
class PhotosActivity : AppCompatActivity() {

    private lateinit var photoBinding: ActivityPhotosBinding

    var modelist=ArrayList<PhotoModel>()

    var images= intArrayOf(R.drawable.idli_vada,R.drawable.kabab)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoBinding = ActivityPhotosBinding.inflate(layoutInflater)
        setContentView(photoBinding.root)

        setToolbar()


        val gridView=findViewById<GridView>(R.id.gridView)

        for (i in images.indices){
            modelist.add(PhotoModel(images[i]))
        }

        var customAdapter=customAdapter(modelist,this)

        gridView.adapter=customAdapter

        gridView.setOnItemClickListener { parent, view, position, id ->
            val intent= Intent(this,PhotosDetailsActivity::class.java)
            intent.putExtra("data",modelist[position])
            startActivity(intent)
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

            Toast.makeText(this,"camera",Toast.LENGTH_LONG).show()


            true
        }
    }
    private fun addPhotos(){

        val takePictureIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(this.packageManager)!=null){
            startActivityForResult(takePictureIntent, REQUEST_CODE)
        }
        else{
            Toast.makeText(this,"unable to open camera", Toast.LENGTH_LONG).show()
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
        var itemModel:ArrayList<PhotoModel>,
        var context: Context
    ): BaseAdapter(){

        var layoutInflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getCount(): Int {
            return itemModel.size
        }

        override fun getItem(position: Int): Any {
            return itemModel[position]
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

            imageView?.setImageResource(itemModel[position].image!!)


            return view!!

        }
    }
}