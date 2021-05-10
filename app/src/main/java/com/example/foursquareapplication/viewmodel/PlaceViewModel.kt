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
import androidx.paging.PagingData
import com.example.foursquareapplication.datasource.PlaceDataSourceFactory
import com.example.foursquareapplication.datasource.ReviewDataSource
import com.example.foursquareapplication.datasource.ReviewDataSourceFactory
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.ReviewData
import com.example.foursquareapplication.repository.PlaceRepository

class PlaceViewModel() : ViewModel() {

    private val placeRepository = PlaceRepository()

    var itemPagedList: LiveData<PagedList<DataPlace>>? = null
    private var locationData : MutableLiveData<Location> = MutableLiveData()

    fun getPlaceDetails(type: String, latitude: Double,longitude: Double): LiveData<PagedList<DataPlace>>? {

        val itemDataSourceFactory = PlaceDataSourceFactory(type,latitude,longitude)

        val liveDataSource = itemDataSourceFactory.itemLiveDataSource

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(ReviewDataSource.PAGE_SIZE).build()

        itemPagedList = LivePagedListBuilder(itemDataSourceFactory,pagedListConfig).build()
        return itemPagedList
    }

    var placeData : LiveData<PagingData<DataPlace>> = MutableLiveData()

    fun getLocationData(type: String,latitude: Double,longitude: Double): LiveData<PagingData<DataPlace>> {
        placeData = placeRepository.getPlaceData(type,latitude, longitude)
        return placeData

    }




}
