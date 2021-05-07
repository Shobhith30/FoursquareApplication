package com.example.foursquareapplication.viewmodel

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.example.foursquareapplication.datasource.PlaceDataSourceFactory
import com.example.foursquareapplication.datasource.ReviewDataSource
import com.example.foursquareapplication.datasource.ReviewDataSourceFactory
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.ReviewData
import com.example.foursquareapplication.repository.PlaceRepository

class PlaceViewModel() : ViewModel() {


    var itemPagedList: LiveData<PagedList<DataPlace>>? = null
    private var locationData : MutableLiveData<Location> = MutableLiveData()

    fun getPlaceDetails(type: String, latitude: Double,longitude: Double): LiveData<PagedList<DataPlace>>? {
        Log.d("here-v",type)
        val itemDataSourceFactory = PlaceDataSourceFactory(type,latitude,longitude)

        //getting the live data source from data source factory
        val liveDataSource = itemDataSourceFactory.itemLiveDataSource

        //Getting PagedList config
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false).setInitialLoadSizeHint(5)
            .setPageSize(ReviewDataSource.PAGE_SIZE).build()

        //Building the paged list
        itemPagedList = LivePagedListBuilder(itemDataSourceFactory,pagedListConfig).build()
        return itemPagedList
    }


}
