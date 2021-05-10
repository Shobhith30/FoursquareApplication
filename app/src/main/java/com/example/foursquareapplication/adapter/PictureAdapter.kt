package com.example.foursquareapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ItemPhotosBinding
import com.example.foursquareapplication.databinding.ItemReviewBinding
import com.example.foursquareapplication.model.PhotoData
import com.example.foursquareapplication.model.ReviewData
import com.example.foursquareapplication.ui.PhotosDetailsActivity

class PictureAdapter (private val mCtx: Context) :
    PagingDataAdapter<PhotoData, PictureAdapter.ItemViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater =
            LayoutInflater.from(parent.context)
        val photosBinding = ItemPhotosBinding.inflate(inflater,parent,false)
        return ItemViewHolder(photosBinding)    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {


            Glide.with(mCtx).load(item.getImageUrl()).placeholder(R.drawable.loading).into(holder.photosBinding.image1)

            holder.photosBinding.image1.setOnClickListener {
                val intent=Intent(it.context,PhotosDetailsActivity::class.java)
                intent.putExtra("photoId",item.getphotoId())
                it.context.startActivity(intent)
            }

        } else {
            println("ge"+ item?.getImageUrl())

            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show()
        }    }

    inner class ItemViewHolder(val photosBinding: ItemPhotosBinding) :
        RecyclerView.ViewHolder(photosBinding.root) {

    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<PhotoData> =
            object : DiffUtil.ItemCallback<PhotoData>() {
                override fun areItemsTheSame(
                    oldItem:PhotoData,
                    newItem: PhotoData
                ): Boolean {
                    return oldItem.getphotoId() == newItem.getphotoId()
                }

                override fun areContentsTheSame(
                    oldItem: PhotoData,
                    newItem: PhotoData
                ): Boolean {
                    return oldItem.getphotoId() == newItem.getphotoId()
                }
            }
    }
}