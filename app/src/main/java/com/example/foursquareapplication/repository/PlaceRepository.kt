package com.example.foursquareapplication.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.foursquareapplication.datasource.LocationDataSource
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.network.FourSquareApiInstance
import com.example.foursquareapplication.network.PlaceApi

class PlaceRepository {

    val placeApi = FourSquareApiInstance.getApiInstance(PlaceApi::class.java)

    fun getPlaceData(type : String, latitude : Double,longitude : Double): LiveData<PagingData<DataPlace>> {
        return Pager(
            config = PagingConfig(
                pageSize = 3,
                maxSize = 500,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { LocationDataSource(placeApi,type,latitude, longitude) }
        ).liveData
    }
}