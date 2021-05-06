package com.example.foursquareapplication.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.foursquareapplication.model.PhotoData
import com.example.foursquareapplication.model.ReviewData

class PicturesDataSourseFactory(val placeId : Int) : DataSource.Factory<Int, PhotoData>() {

    val itemLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, PhotoData>>()


    override fun create(): DataSource<Int, PhotoData> {
        //getting our data source object
        val itemDataSource = PicturesDataSource(placeId)

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource)
        Log.d("here",itemDataSource.toString())

        //returning the datasource
        return itemDataSource
    }
}