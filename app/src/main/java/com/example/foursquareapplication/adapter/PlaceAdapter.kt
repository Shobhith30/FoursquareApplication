package com.example.foursquareapplication.adapter

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ItemPlaceBinding
import com.example.foursquareapplication.databinding.ItemReviewBinding
import com.example.foursquareapplication.helper.ChangeRatingColor
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.Review
import com.example.foursquareapplication.model.ReviewData
import com.example.foursquareapplication.ui.DetailsActivity
import kotlin.math.round


class PlaceAdapter (private val mCtx: Context) :
    PagedListAdapter<DataPlace, PlaceAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater =
            LayoutInflater.from(parent.context)
        val placeBinding = ItemPlaceBinding.inflate(inflater,parent,false)
        return ItemViewHolder(placeBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {

            item.getPlace()?.let {
                holder.placeBinding.name.text = it.getName()
                Glide.with(mCtx).load(it.getImage()).placeholder(R.drawable.loading).into(holder.placeBinding.placeImage)
                val rating = it.getOverallRating()
                if(rating!=0.0f) {
                    holder.placeBinding.ratingBackground.visibility = View.VISIBLE
                    holder.placeBinding.rating.text = item.getPlace()?.getOverallRating().toString()
                    val ratingBackground = ChangeRatingColor().getRatingColor(
                        rating)
                    holder.placeBinding.ratingBackground.setBackgroundColor(ratingBackground)

                }
                holder.placeBinding.address.text = it.getLandmark()
            }
            holder.placeBinding.distance.text = String.format("%.1f km", item.getDistance())


        } else {
            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show()
        }
    }

    inner class ItemViewHolder(val placeBinding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(placeBinding.root) {
            init {
                placeBinding.root.setOnClickListener {
                    val intent = Intent(mCtx,DetailsActivity::class.java)
                    val bundle = Bundle()
                    val item = getItem(bindingAdapterPosition)
                    bundle.putParcelable(Constants.PLACE_RESPOSNE,item)
                    intent.putExtras(bundle)
                    mCtx.startActivity(intent)
                }
            }

    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<DataPlace> =
            object : DiffUtil.ItemCallback<DataPlace>() {
                override fun areItemsTheSame(
                    oldItem:DataPlace,
                    newItem: DataPlace
                ): Boolean {
                    return oldItem.getPlace()?.getPlaceId() == newItem.getPlace()?.getPlaceId()
                }

                override fun areContentsTheSame(
                    oldItem: DataPlace,
                    newItem: DataPlace
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}