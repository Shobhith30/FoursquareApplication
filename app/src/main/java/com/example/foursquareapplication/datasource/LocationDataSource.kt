package com.example.foursquareapplication.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.Place
import com.example.foursquareapplication.network.FourSquareApiInstance
import com.example.foursquareapplication.network.PlaceApi
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class LocationDataSource(private val placeApi : PlaceApi,private val type :String,
private val latitude : Double, private val longitude  :Double):
    PagingSource<Int, DataPlace>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataPlace> {
        val position = params.key ?: 0
        return try {
            val response =
                placeApi.getPlaceData(type, latitude, longitude, position, params.loadSize)
            val place = response.getData()
            LoadResult.Page (
                data = place ?: emptyList(),
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (place==null) null else position + 1
            )
        }catch (exception : IOException){
            LoadResult.Error(exception)
        }catch (exception : HttpException){
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DataPlace>): Int? {
       return state?.anchorPosition
    }
}