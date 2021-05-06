package com.example.foursquareapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.example.foursquareapplication.datasource.FavouriteDataSource
import com.example.foursquareapplication.datasource.FavouriteDataSourceFactory
import com.example.foursquareapplication.datasource.ReviewDataSource
import com.example.foursquareapplication.datasource.ReviewDataSourceFactory
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.Place
import com.example.foursquareapplication.model.ReviewData

class FavouriteViewModel(application: Application)  : AndroidViewModel(application) {

    //creating livedata for PagedList  and PagedKeyedDataSource
    var itemPagedList: LiveData<PagedList<DataPlace>>? = null
    var liveDataSource: LiveData<PageKeyedDataSource<Int, DataPlace>>? = null

    fun getFavourite(userId : Int , token : String)  : LiveData<PagedList<DataPlace>>?{
        val itemDataSourceFactory = FavouriteDataSourceFactory(userId,token)

        //getting the live data source from data source factory
        liveDataSource = itemDataSourceFactory.itemLiveDataSource

        //Getting PagedList config
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false).setInitialLoadSizeHint(5)
            .setPageSize(ReviewDataSource.PAGE_SIZE).build()

        //Building the paged list
        itemPagedList = LivePagedListBuilder(itemDataSourceFactory,pagedListConfig).build()
        return itemPagedList
    }
}