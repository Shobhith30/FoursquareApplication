package com.example.foursquareapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import com.example.foursquareapplication.databinding.ActivityPhotosBinding

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