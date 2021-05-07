package com.example.foursquareapplication.datasource

import android.content.ClipData
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.ReviewData


class PlaceDataSourceFactory(val type : String,val latitude : Double,val longitude : Double) : DataSource.Factory<Int, DataPlace>() {
    //getter for itemlivedatasource
    //creating the mutable live data
    val itemLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, DataPlace>>()

    override fun create(): DataSource<Int, DataPlace> {
        //getting our data source object
        val itemDataSource = PlaceDataSource(type,latitude, longitude)

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource)
        Log.d("here",itemDataSource.type)

        //returning the datasource
        return itemDataSource
    }
}