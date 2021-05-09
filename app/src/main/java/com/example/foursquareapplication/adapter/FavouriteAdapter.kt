package com.example.foursquareapplication.adapter

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foursquareapplication.OnFavouriteCLickListener
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ItemFavouritesBinding
import com.example.foursquareapplication.helper.ChangeRatingColor
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.helper.PlaceUtils
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.Place
import com.example.foursquareapplication.ui.DetailsActivity
import com.example.foursquareapplication.ui.FavouriteActivity
import com.example.foursquareapplication.viewmodel.FavouriteViewModel
import com.google.android.gms.location.LocationServices

class FavouriteAdapter(private val mCtx: Context) :
    PagedListAdapter<Place, FavouriteAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    private var listener : OnFavouriteCLickListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val favouriteBinding = ItemFavouritesBinding.inflate(inflater,parent,false)
        return ItemViewHolder(favouriteBinding)

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {

            holder.favouriteBinding.nameFavourite.text = item.getName()
            Glide.with(mCtx).load(item.getImage()).placeholder(R.drawable.loading).into(holder.favouriteBinding.placeImageFavourite)
            val rating = item.getOverallRating()
            if (rating != 0.0f) {
                holder.favouriteBinding.ratingBackgroundFavourite.visibility = View.VISIBLE
                holder.favouriteBinding.ratingFavourite.text = String.format("%.1f",item.getOverallRating())
                val ratingBackground = ChangeRatingColor().getRatingColor(
                        rating)
                holder.favouriteBinding.ratingBackgroundFavourite.setBackgroundColor(ratingBackground)

            }
            val cost = PlaceUtils().getCost(item.getCost(), holder.favouriteBinding.priceRangeFavourite.context)
            if (cost == null) {
                holder.favouriteBinding.dotFavourite.visibility = View.GONE
            } else {
                holder.favouriteBinding.priceRangeFavourite.text = cost
            }
            if (item.getPlaceType().size > 0) {
                val type = PlaceUtils().getShortPlaceType(item.getPlaceType()[0].getCategoryName())
                holder.favouriteBinding.typeFavourite.text = type
            } else {
                holder.favouriteBinding.typeFavourite.visibility = View.GONE
            }
            holder.favouriteBinding.addressFavourite.text = item.getLandmark()
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(mCtx)
            if (ActivityCompat.checkSelfPermission(
                            mCtx,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mCtx,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            fusedLocationClient.lastLocation.addOnSuccessListener(mCtx as AppCompatActivity) { location ->
                if (location != null) {
                    val results = FloatArray(2)
                    Location.distanceBetween(
                            item.getLatitude(), item.getLongitude(),
                            location.latitude, location.longitude,
                            results)
                    holder.favouriteBinding.distanceFavourite.text = String.format("%.1f Km", results[0] / 1000)
                }
            }




            holder.removeFavourite.setOnClickListener {
                listener?.removeFavourite(item.getPlaceId())

            }
        } else {
            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show()
        }
    }

    inner class ItemViewHolder(val favouriteBinding: ItemFavouritesBinding) : RecyclerView.ViewHolder(favouriteBinding.root) {

            val landmark = favouriteBinding.nameFavourite
            val priceRange = favouriteBinding.priceRangeFavourite
            val image = favouriteBinding.placeImageFavourite
            val favouriteType = favouriteBinding.typeFavourite
            val address = favouriteBinding.addressFavourite
            val rating = favouriteBinding.ratingFavourite
            val removeFavourite = favouriteBinding.removeFavourite

        init {
            favouriteBinding.root.setOnClickListener {
                val intent = Intent(mCtx, DetailsActivity::class.java)
                val bundle = Bundle()
                val item = getItem(bindingAdapterPosition)
                bundle.putParcelable(Constants.PLACE_RESPOSNE,item)
                bundle.putBoolean(Constants.IS_FAVOURITE,true)
                intent.putExtras(bundle)
                mCtx.startActivity(intent)
            }

        }

    }
    fun setListener(listener : OnFavouriteCLickListener){
        this.listener = listener
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