package com.example.foursquareapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ItemPhotosBinding
import com.example.foursquareapplication.databinding.ItemReviewBinding
import com.example.foursquareapplication.model.PhotoData
import com.example.foursquareapplication.model.ReviewData

class PictureAdapter (private val mCtx: Context) :
    PagedListAdapter<PhotoData, PictureAdapter.ItemViewHolder>(PictureAdapter.DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater =
            LayoutInflater.from(parent.context)
        val photosBinding = ItemPhotosBinding.inflate(inflater,parent,false)
        return ItemViewHolder(photosBinding)    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            println("gehhhh"+ item?.getImageUrl())

            holder.photosBinding.image1

            Glide.with(mCtx).load(item.getImageUrl()).placeholder(R.drawable.loading).into(holder.photosBinding.image1)

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
                    return oldItem.getplaceId() == newItem.getplaceId()
                }

                override fun areContentsTheSame(
                    oldItem: PhotoData,
                    newItem: PhotoData
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}