package com.example.foursquareapplication.datasource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.*
import com.example.foursquareapplication.network.FavouriteApi
import com.example.foursquareapplication.network.FourSquareApiInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.Place
import com.example.foursquareapplication.network.PlaceApi
import retrofit2.HttpException
import java.io.IOException

class FavouriteDataSource(private val favouriteApi : FavouriteApi,val query : String,val userId: Int, val token: String):
    PagingSource<Int, Place>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Place> {
        val position = params.key ?: 0
        return try {
            val response =
                favouriteApi.getFavouriteData(userId, position, params.loadSize,token)
            val place = response.getData()
            LoadResult.Page (
                data = filterData(place)?: emptyList(),
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (place==null) null else position + 1
            )

        }catch (exception : IOException){
            LoadResult.Error(exception)
        }catch (exception : HttpException){
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Place>): Int? {
        return state?.anchorPosition
    }


    fun filterData(data :List<Place>?): List<Place>? {
        val temp = arrayListOf<Place>()
        return if(query == ""){
            Log.d("original",data.toString())
            data

        }else{
            if (data != null) {
                for(i in data){
                    if(i.getName().contains(query,true)){
                        if(!temp.contains(i)) {
                            temp.add(i)
                            Log.e("here", temp.toString())
                        }
                    }
                }
            }
            temp
        }
    }




    companion object {
        //the size of a page that we want
        const val PAGE_SIZE = 3

        //we will start from the first page which is 1
        private const val FIRST_PAGE = 0

    }
}