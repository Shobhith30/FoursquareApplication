package com.example.foursquareapplication.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquareapplication.OnFavouriteCLickListener
import com.example.foursquareapplication.R
import com.example.foursquareapplication.adapter.FavouriteAdapter
import com.example.foursquareapplication.adapter.ReviewAdapter
import com.example.foursquareapplication.databinding.ActivityFavouriteBinding
import com.example.foursquareapplication.databinding.ActivityReviewBinding
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.Place
import com.example.foursquareapplication.model.PlaceResponse
import com.example.foursquareapplication.ui.authentication.ForgotPassword
import com.example.foursquareapplication.viewmodel.AuthenticationViewModel
import com.example.foursquareapplication.viewmodel.FavouriteViewModel
import com.example.foursquareapplication.viewmodel.ReviewViewModel

class FavouriteActivity : AppCompatActivity(),OnFavouriteCLickListener {

    private lateinit var favouriteBinding: ActivityFavouriteBinding
    private lateinit var favouriteAdapter: FavouriteAdapter
    private lateinit var favouriteViewModel: FavouriteViewModel
    private var favList: PagedList<Place>? = null
    private var pagedList :  MutableLiveData<PagedList<Place>> = MutableLiveData()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favouriteBinding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(favouriteBinding.root)

        supportActionBar?.hide()
        favouriteBinding.searchFavourite.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                favouriteViewModel.text.value = query.toString()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val sharedPreferences = getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE)
                val token = sharedPreferences.getString(Constants.USER_TOKEN, "")
                val newToken = "Bearer $token"
                val userId = sharedPreferences.getString(Constants.USER_ID, "")?.toInt()
                favouriteViewModel.getFavourite(newText.toString(), userId!!, newToken)
                favouriteViewModel.getItemPageList()?.observe(this@FavouriteActivity, {
                    favouriteAdapter.submitList(it)
                })

                return true
            }

        })
        val toolbar = favouriteBinding.toolbarFavourite
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
            return@setNavigationOnClickListener
        }

        favouriteViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(FavouriteViewModel::class.java)
        favouriteAdapter = FavouriteAdapter(this)
        favouriteAdapter.setListener(this)
        favouriteBinding.favouriteRecyclerView.layoutManager = LinearLayoutManager(this)
        favouriteBinding.favouriteRecyclerView.adapter = favouriteAdapter

        val sharedPreferences = getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE)
        val token = sharedPreferences.getString(Constants.USER_TOKEN, "")
        val newToken = "Bearer $token"
        val userId = sharedPreferences.getString(Constants.USER_ID, "")?.toInt()

        if (userId != null) {
            favouriteViewModel.getFavourite("", userId, newToken)
            favouriteViewModel.getItemPageList()?.observe(this) {

                if (it.size > 0) {
                    if (it.get(0) != null) {
                        pagedList.value = it
                        //favouriteAdapter.submitList(it)

                    }
                }
            }
        }




        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(applicationContext.resources.getDrawable(R.drawable.divider))
        favouriteBinding.favouriteRecyclerView.addItemDecoration(divider)

        favouriteViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(FavouriteViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favourite, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        getFavourite()
    }
    fun getFavourite(){
        pagedList.observe(this){
            if (it.size > 0) {
                if (it.get(0) != null) {
                    favouriteAdapter = FavouriteAdapter(this)
                    favouriteAdapter.setListener(this)
                    favouriteBinding.favouriteRecyclerView.layoutManager = LinearLayoutManager(this)
                    favouriteBinding.favouriteRecyclerView.adapter = favouriteAdapter
                    favouriteAdapter.submitList(it)
                    it.dataSource.invalidate()
                }
            }
        }
    }

    fun performFilter(query: String?) {
        var data: PagedList<Place>? = favList
        if (favList != null) {
            Log.d("Navya S", favList.toString())
            if (query != null) {
                for (i in favList!!) {

                    if (!(i.getName().contains(query, true)))
                        Log.d("Navya S", "match")
                    data?.remove(i)
                    Log.d("Navya S", "matched $data")
                }
            } else {
                data = favList
            }
        }
        val list = favouriteAdapter.currentList
        list?.removeAt(0)
    }

    override fun onFavouriteClick(isFav: Boolean, id: Int?) {}

    override fun removeFavourite(id: Int?) {
       deleteFavourite(id)
    }

    private fun deleteFavourite(id: Int?) {
        val placeId = id
        val sharedPreferences = getSharedPreferences(Constants.USER_PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        val token = sharedPreferences.getString(Constants.USER_TOKEN, "")
        val newToken = "Bearer $token"
        val userId = sharedPreferences.getString(Constants.USER_ID, "").toString()

        val userFavourite = hashMapOf("userId" to userId, "placeId" to placeId.toString())
        val favouriteViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(FavouriteViewModel::class.java)

        if (placeId != null) {
            favouriteViewModel.deleteFavourite(newToken, userFavourite).observe(this) {
                if (it.getStatus() == Constants.STATUS_OK) {
                    getFavourite()
                    Toast.makeText(this, it.getMessage(), Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(applicationContext, it.getMessage(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}