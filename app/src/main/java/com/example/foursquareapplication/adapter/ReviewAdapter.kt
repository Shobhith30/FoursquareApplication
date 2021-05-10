package com.example.foursquareapplication.adapter

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ItemReviewBinding
import com.example.foursquareapplication.model.Review
import com.example.foursquareapplication.model.ReviewData


class ReviewAdapter (private val mCtx: Context) :
    PagingDataAdapter<ReviewData, ReviewAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater =
            LayoutInflater.from(parent.context)
        val reviewBinding = ItemReviewBinding.inflate(inflater,parent,false)
        return ItemViewHolder(reviewBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {

            holder.bind(item)
        }
    }

    inner class ItemViewHolder(val reviewBinding: ItemReviewBinding) :
        RecyclerView.ViewHolder(reviewBinding.root) {

            fun bind(item : ReviewData){
                reviewBinding.apply {
                   name.text = item.getUserName()
                    reviewDate.text = item.getDate()
                    reviewText.text = item.getReview()
                    Glide.with(mCtx).load(item.getUserImage()).placeholder(R.drawable.loading).into(profilePicture)
                }
            }

    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ReviewData> =
            object : DiffUtil.ItemCallback<ReviewData>() {
                override fun areItemsTheSame(
                    oldItem:ReviewData,
                    newItem: ReviewData
                ): Boolean {
                    return oldItem.getPlaceId() == newItem.getPlaceId()
                }

                override fun areContentsTheSame(
                    oldItem: ReviewData,
                    newItem: ReviewData
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}