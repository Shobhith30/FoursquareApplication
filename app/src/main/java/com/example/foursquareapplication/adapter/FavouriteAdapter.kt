package com.example.foursquareapplication.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ItemFavouritesBinding
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.Place

class FavouriteAdapter(private val mCtx: Context) :
    PagedListAdapter<Place, FavouriteAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val favouriteBinding = ItemFavouritesBinding.inflate(inflater,parent,false)
        return ItemViewHolder(favouriteBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            Log.i("Navya","Adapter : $item")
            holder.favouriteType.text = item.getPlaceType().toString()
            holder.priceRange.text = item.getCost().toString()
            holder.landmark.text = item.getLandmark()
            Glide.with(mCtx).load(item.getImage()).into(holder.image)
        } else {
            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show()
        }
    }

    inner class ItemViewHolder(val favouriteBinding: ItemFavouritesBinding) : RecyclerView.ViewHolder(favouriteBinding.root) {

            val landmark = favouriteBinding.nameFavourite
            val priceRange = favouriteBinding.priceRangeFavourite
            val image = favouriteBinding.placeImageFavourite
            val favouriteType = favouriteBinding.typeFavourite
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Place> =
            object : DiffUtil.ItemCallback<Place>() {
                override fun areItemsTheSame(
                    oldItem: Place,
                    newItem: Place
                ): Boolean {
                    return oldItem.getPlaceId() == newItem.getPlaceId()
                }

                override fun areContentsTheSame(
                    oldItem: Place,
                    newItem: Place
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

}