package com.example.foursquareapplication.datasource

import android.content.ClipData
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.foursquareapplication.model.ReviewData


class ReviewDataSourceFactory(val placeId : Int) : DataSource.Factory<Int, ReviewData>() {
    //getter for itemlivedatasource
    //creating the mutable live data
    val itemLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, ReviewData>>()

    override fun create(): DataSource<Int, ReviewData> {
        //getting our data source object
        val itemDataSource = ReviewDataSource(placeId)

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource)
        Log.d("here",itemDataSource.toString())

        //returning the datasource
        return itemDataSource
    }
}