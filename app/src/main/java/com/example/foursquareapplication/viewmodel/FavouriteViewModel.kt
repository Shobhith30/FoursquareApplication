package com.example.foursquareapplication.viewmodel

import android.app.Application
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.arch.core.util.Function
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.example.foursquareapplication.datasource.FavouriteDataSourceFactory
import com.example.foursquareapplication.datasource.ReviewDataSource
import com.example.foursquareapplication.model.Place
import android.util.Log
import com.example.foursquareapplication.datasource.GetFavouriteDataSourceFactory


class FavouriteViewModel(application: Application)  : AndroidViewModel(application) {

    //creating livedata for PagedList  and PagedKeyedDataSource
    var itemPagedList: LiveData<PagedList<Place>>? = null
    var liveDataSource: LiveData<PageKeyedDataSource<Int, Place>>? = null
    var text : MutableLiveData<String> = MutableLiveData()

    fun getFavourite(query: String,userId : Int , token : String) {
        val itemDataSourceFactory = FavouriteDataSourceFactory(query,userId,token)

        //getting the live data source from data source factory
        liveDataSource = itemDataSourceFactory.itemLiveDataSource

        //Getting PagedList config
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false).setInitialLoadSizeHint(5)
            .setPageSize(ReviewDataSource.PAGE_SIZE).build()

        //Building the paged list
        itemPagedList = LivePagedListBuilder(itemDataSourceFactory,pagedListConfig).build()

    }
    fun setFilter(query: String,userId : Int , token : String): LiveData<PagedList<Place>> {
        val itemDataSourceFactory = GetFavouriteDataSourceFactory(query,userId,token)

        //getting the live data source from data source factory
        val liveDataSource = itemDataSourceFactory.itemLiveDataSource

        //Getting PagedList config
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false).setInitialLoadSizeHint(5)
                .setPageSize(ReviewDataSource.PAGE_SIZE).build()

        //Building the paged list
        val pageList = LivePagedListBuilder(itemDataSourceFactory,pagedListConfig).build()
        return pageList

    }

    fun getItemPageList() = itemPagedList



}