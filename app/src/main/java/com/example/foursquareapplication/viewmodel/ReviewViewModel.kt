package com.example.foursquareapplication.viewmodel

import android.app.Application
import android.content.ClipData.Item
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.example.foursquareapplication.datasource.ReviewDataSource
import com.example.foursquareapplication.datasource.ReviewDataSourceFactory
import com.example.foursquareapplication.model.Rating
import com.example.foursquareapplication.model.ReviewData
import com.example.foursquareapplication.repository.ReviewRepository


class ReviewViewModel(application: Application)  : AndroidViewModel(application) {

    //creating livedata for PagedList  and PagedKeyedDataSource
    val reviewRepository = ReviewRepository(application)
    var itemPagedList: LiveData<PagedList<ReviewData>>? = null
    var liveDataSource: LiveData<PageKeyedDataSource<Int, ReviewData>>? = null

    //constructor
    init {
        //getting our data source factory

    }
    fun getReview(placeId : Int)  : LiveData<PagedList<ReviewData>>?{
        val itemDataSourceFactory = ReviewDataSourceFactory(placeId)

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

    fun addRating(token : String,rating : HashMap<String,String>) : LiveData<Rating>{
        return reviewRepository.addRating(token,rating)
    }
}