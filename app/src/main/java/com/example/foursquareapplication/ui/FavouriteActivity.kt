package com.example.foursquareapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquareapplication.R
import com.example.foursquareapplication.adapter.FavouriteAdapter
import com.example.foursquareapplication.adapter.ReviewAdapter
import com.example.foursquareapplication.databinding.ActivityFavouriteBinding
import com.example.foursquareapplication.databinding.ActivityReviewBinding
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.Place
import com.example.foursquareapplication.model.PlaceResponse
import com.example.foursquareapplication.viewmodel.FavouriteViewModel
import com.example.foursquareapplication.viewmodel.ReviewViewModel

class FavouriteActivity : AppCompatActivity() {

    private lateinit var favouriteBinding: ActivityFavouriteBinding
    private lateinit var favouriteAdapter: FavouriteAdapter
    private lateinit var favouriteViewModel: FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favouriteBinding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(favouriteBinding.root)

        supportActionBar?.hide()

        val toolbar = favouriteBinding.toolbarFavourite
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
            return@setNavigationOnClickListener
        }

        favouriteViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(FavouriteViewModel::class.java)
        favouriteAdapter = FavouriteAdapter(this)
        favouriteBinding.favouriteRecyclerView.layoutManager = LinearLayoutManager(this)
        favouriteBinding.favouriteRecyclerView.adapter = favouriteAdapter

        val sharedPreferences = getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE)
        val token = sharedPreferences.getString(Constants.USER_TOKEN,"")
        if (token != null) {
            favouriteViewModel.getFavourite(81,token)?.observe(this,{
                if (it != null) {
                    Log.i("Navya","$token")
                    Log.i("Navya","$it")
                    Toast.makeText(this,"Favourite", Toast.LENGTH_LONG).show()

                } else {
                    Toast.makeText(applicationContext, "$it", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favourite, menu)
        return super.onCreateOptionsMenu(menu)
    }
}