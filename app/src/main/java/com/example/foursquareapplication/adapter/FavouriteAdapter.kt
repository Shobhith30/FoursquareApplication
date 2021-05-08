package com.example.foursquareapplication.adapter

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ItemFavouritesBinding
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.Place
import com.example.foursquareapplication.ui.DetailsActivity
import com.example.foursquareapplication.ui.FavouriteActivity
import com.example.foursquareapplication.viewmodel.FavouriteViewModel

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
            Log.i("Navya","position : $position")
            //Toast.makeText(mCtx, "Item is $item", Toast.LENGTH_LONG).show()
            holder.favouriteType.text = item.getPlaceType().toString()
           // holder.favouriteType.text = item.getPlaceType().get(0).getCategoryName()
            //Log.i("Navya", item.getPlaceType().get(0).getCategoryName())
            holder.priceRange.text = item.getCost().toString()
            holder.landmark.text = item.getName()
            holder.address.text = item.getAddress()
            Glide.with(mCtx).load(item.getImage()).placeholder(R.drawable.loading).into(holder.image)

            holder.removeFavourite.setOnClickListener {
                val placeId = item.getPlaceId()
                val sharedPreferences = mCtx.getSharedPreferences(Constants.USER_PREFERENCE, AppCompatActivity.MODE_PRIVATE)
                val token = sharedPreferences.getString(Constants.USER_TOKEN, "")
                val newToken = "Bearer $token"
                val userId = sharedPreferences.getString(Constants.USER_ID, "").toString()

                val userFavourite = hashMapOf("userId" to userId, "placeId" to placeId.toString())
                val favouriteViewModel = ViewModelProvider.AndroidViewModelFactory(mCtx.applicationContext as Application).create(FavouriteViewModel::class.java)

                if (placeId != 0) {
                    favouriteViewModel.deleteFavourite(newToken, userFavourite).observeForever({
                        if (it.getStatus() == Constants.STATUS_OK) {
                            Toast.makeText(mCtx, it.getMessage(), Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(mCtx, it.getMessage(), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        } else {
            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show()
        }
    }

    inner class ItemViewHolder(favouriteBinding: ItemFavouritesBinding) : RecyclerView.ViewHolder(favouriteBinding.root) {

            val landmark = favouriteBinding.nameFavourite
            val priceRange = favouriteBinding.priceRangeFavourite
            val image = favouriteBinding.placeImageFavourite
            val favouriteType = favouriteBinding.typeFavourite
            val address = favouriteBinding.addressFavourite

            val removeFavourite = favouriteBinding.removeFavourite

        init {
            favouriteBinding.root.setOnClickListener {
                val intent = Intent(mCtx, DetailsActivity::class.java)
                val bundle = Bundle()
                val item = getItem(bindingAdapterPosition)
                bundle.putParcelable(Constants.PLACE_RESPOSNE,item)
                intent.putExtras(bundle)
                mCtx.startActivity(intent)
            }

        }

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