package com.example.foursquareapplication.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquareapplication.R
import com.example.foursquareapplication.adapter.FavouriteAdapter
import com.example.foursquareapplication.adapter.ReviewAdapter
import com.example.foursquareapplication.databinding.ActivityFavouriteBinding
import com.example.foursquareapplication.databinding.ActivityReviewBinding
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.Place
import com.example.foursquareapplication.model.PlaceResponse
import com.example.foursquareapplication.viewmodel.FavouriteViewModel
import com.example.foursquareapplication.viewmodel.ReviewViewModel

class FavouriteActivity : AppCompatActivity() {

    private lateinit var favouriteBinding: ActivityFavouriteBinding
    private lateinit var favouriteAdapter: FavouriteAdapter
    private lateinit var favouriteViewModel: FavouriteViewModel
    private var favList : PagedList<Place >?=null

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favouriteBinding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(favouriteBinding.root)

        supportActionBar?.hide()
        favouriteBinding.searchFavourite.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
               favouriteViewModel.text.value = query.toString()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val sharedPreferences = getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE)
                val token = sharedPreferences.getString(Constants.USER_TOKEN,"")
                val newToken = "Bearer $token"
                val userId = sharedPreferences.getString(Constants.USER_ID,"")?.toInt()
               favouriteViewModel.getFavourite(newText.toString(),userId!!,newToken)
                favouriteViewModel.getItemPageList()?.observe(this@FavouriteActivity,{
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
        favouriteBinding.favouriteRecyclerView.layoutManager = LinearLayoutManager(this)
        favouriteBinding.favouriteRecyclerView.adapter = favouriteAdapter

        val sharedPreferences = getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE)
        val token = sharedPreferences.getString(Constants.USER_TOKEN,"")
        val newToken = "Bearer $token"
        val userId = sharedPreferences.getString(Constants.USER_ID,"")?.toInt()

        if (userId != null) {
            favouriteViewModel.getFavourite("",userId,newToken)
                    favouriteViewModel.getItemPageList()?.observe(this) {
                Log.i("Navya","userId : $userId and $newToken")
                Log.i("Navya","$it")
                Toast.makeText(this,"Favourite", Toast.LENGTH_LONG).show()
                if (it.size > 0) {
                    favList = it
                    Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
                    favouriteAdapter.submitList(it)

                }
            }
        }

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(applicationContext.resources.getDrawable(R.drawable.divider))
        favouriteBinding.favouriteRecyclerView.addItemDecoration(divider)
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favourite, menu)
        return super.onCreateOptionsMenu(menu)
    }
    fun performFilter(query : String?){
        var data : PagedList<Place>? = favList
        if(favList!=null){
            Log.d("Navya S",favList.toString())
                if (query != null) {
                    for(i in favList!!) {

                        if (!(i.getName().contains(query, true)))
                            Log.d("Navya S","match")
                            data?.remove(i)
                         Log.d("Navya S","matched $data")
                    }
                }else{
                    data = favList                   }
            }
            val list = favouriteAdapter.currentList
        list?.removeAt(0)
        }

}