package com.example.foursquareapplication.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.PageKeyedDataSource
import com.example.foursquareapplication.model.*
import com.example.foursquareapplication.network.FavouriteApi
import com.example.foursquareapplication.network.FourSquareApiInstance
import com.example.foursquareapplication.network.ReviewApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavouriteDataSource(val userId : Int,val token : String) : PageKeyedDataSource<Int, DataPlace>() {

    //this will be called once to load the initial data
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DataPlace>
    ) {
        val response = FourSquareApiInstance.getApiInstance(FavouriteApi::class.java)
            .getFavourite(userId, FIRST_PAGE,3,token).execute()

        if(response.body()!=null) {

            callback.onResult(response.body()!!.getData() as List<DataPlace>, null, FIRST_PAGE + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DataPlace>) {
        val response = FourSquareApiInstance.getApiInstance(FavouriteApi::class.java)
            .getFavourite(userId,params.key,3,token)
            .enqueue(object : Callback<PlaceResponse>{
                override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                    val adjacentKey = if (params.key > 0) params.key - 1 else null
                    if (response.body() != null) {

                        callback.onResult(response.body()!!.getData() as List<DataPlace>, adjacentKey)
                    }
                }

                override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {

                }

            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DataPlace>) {
        FourSquareApiInstance.getApiInstance(FavouriteApi::class.java)
            .getFavourite(userId,params.key,3,token)
            .enqueue(object : Callback<PlaceResponse>{
                override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                    if (response.body() != null) {
                        //if the response has next page
                        //incrementing the next page number
                        Log.e("after","after")
                        val key = if (!(response.body()!!.getLastPage())) params.key + 1 else null

                        //passing the loaded data and next page value
                        callback.onResult(response.body()!!.getData() as List<DataPlace>, key)
                    }
                }

                override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {

                }

            })
    }



    companion object {
        //the size of a page that we want
        const val PAGE_SIZE = 3

        //we will start from the first page which is 1
        private const val FIRST_PAGE = 0

    }
}