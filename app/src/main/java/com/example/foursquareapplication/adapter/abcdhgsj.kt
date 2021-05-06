package com.example.foursquareapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ItemPhotosBinding
import com.example.foursquareapplication.model.PhotoData

class PicturesAdapter (private val mCtx: Context) :
PagedListAdapter<PhotoData, PicturesAdapter.ItemViewHolder>(PicturesAdapter.DIFF_CALLBACK) {

    interface ListAdapter{

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater =
            LayoutInflater.from(parent.context)
        val photosBinding = ItemPhotosBinding.inflate(inflater,parent,false)
        return ItemViewHolder(photosBinding)    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {

            holder.photosBinding.image1

            Glide.with(mCtx).load(item.getImageUrl()).placeholder(R.drawable.loading).into(holder.photosBinding.image1)
/*
            Glide.with(mCtx).load(item.getUserImage()).placeholder(R.drawable.loading).into(holder.reviewBinding.profilePicture)
*/


/*
            holder.reviewBinding.name.text = item.getUserName()
            holder.reviewBinding.reviewDate.text = item.getDate()
            holder.reviewBinding.reviewText.text = item.getReview()
            Glide.with(mCtx).load(item.getUserImage()).placeholder(R.drawable.loading).into(holder.reviewBinding.profilePicture)
*/

        } else {
            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show()
        }    }

    inner class ItemViewHolder(val photosBinding: ItemPhotosBinding) :
        RecyclerView.ViewHolder(photosBinding.root) {

    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<PhotoData> =
            object : DiffUtil.ItemCallback<PhotoData>() {
                override fun areItemsTheSame(
                    oldItem: PhotoData,
                    newItem: PhotoData
                ): Boolean {
                    return oldItem.getphotoId() == newItem.getphotoId()
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