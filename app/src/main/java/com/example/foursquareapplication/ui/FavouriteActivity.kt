package com.example.foursquareapplication.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.paging.LoadState
import androidx.paging.PagedList
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquareapplication.OnFavouriteCLickListener
import com.example.foursquareapplication.R
import com.example.foursquareapplication.adapter.FavouriteAdapter
import com.example.foursquareapplication.adapter.FavouriteDataAdapter
import com.example.foursquareapplication.adapter.HeaderAdapter
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
    private lateinit var favouriteAdapter: FavouriteDataAdapter
    private lateinit var favouriteViewModel: FavouriteViewModel
    private var favList: List<Place>? = null
    private var pagedList :  MutableLiveData<PagingData<Place>> = MutableLiveData()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favouriteBinding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(favouriteBinding.root)

        supportActionBar?.hide()
        favouriteBinding.searchFavourite.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //favouriteViewModel.text.value = query.toString()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val sharedPreferences = getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE)
                val token = sharedPreferences.getString(Constants.USER_TOKEN, "")
                val newToken = "Bearer $token"
                val userId = sharedPreferences.getString(Constants.USER_ID, "")?.toInt()
                //favouriteViewModel.getFavourite(newText.toString(), userId!!, newToken)
                favouriteViewModel.getFavouriteData(newText.toString(),userId!!,newToken).observe(this@FavouriteActivity, {
                    favouriteAdapter.submitData(lifecycle,it)
                    Log.e("data",favouriteAdapter.snapshot().items.toString())
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


        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(applicationContext.resources.getDrawable(R.drawable.divider))
        favouriteBinding.favouriteRecyclerView.addItemDecoration(divider)

        favouriteViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(FavouriteViewModel::class.java)
    }

    private fun setAdapter() {
        favouriteAdapter = FavouriteDataAdapter(this)
        favouriteAdapter.setListener(this)
        favouriteBinding.favouriteRecyclerView.layoutManager = LinearLayoutManager(this)
        favouriteBinding.favouriteRecyclerView.adapter = favouriteAdapter
        favouriteBinding.retry.setOnClickListener {
            favouriteAdapter.retry()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favourite, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        setAdapter()
        getFavourite()
    }

    fun getFavourite() {
        val sharedPreferences =
            getSharedPreferences(Constants.USER_PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        val token = sharedPreferences.getString(Constants.USER_TOKEN, "")
        val newToken = "Bearer $token"
        val userId = sharedPreferences.getString(Constants.USER_ID, "").toString()
        //favouriteViewModel.getFavourite("".toString(), userId.toInt(), newToken)

        favouriteViewModel.getFavouriteData("", userId.toInt(), newToken)
            .observe(this@FavouriteActivity) {


                if(it!=null) {

                    favouriteAdapter.submitData(lifecycle, it)

                }
            }






        favouriteAdapter.addLoadStateListener { loadState->


            favouriteBinding.apply {

                progress.isVisible = loadState.source.refresh is LoadState.Loading
                favouriteRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                retry.isVisible = loadState.source.refresh is LoadState.Error
                errorMessage.isVisible = loadState.source.refresh is LoadState.Error
                if(loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached
                    && favouriteAdapter.itemCount<1){
                    favouriteRecyclerView.isVisible  =false
                    noData.isVisible = true
                }else{
                    noData.isVisible = false
                }

            }
        }

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
                if(it!=null) {
                    if (it.getStatus() == Constants.STATUS_OK) {
                        startActivity(Intent(this,FavouriteActivity::class.java))
                        Toast.makeText(this, it.getMessage(), Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(applicationContext, it.getMessage(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}