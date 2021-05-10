package com.example.foursquareapplication.datasource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.foursquareapplication.model.PhotoData
import com.example.foursquareapplication.model.Photos
import com.example.foursquareapplication.model.Review
import com.example.foursquareapplication.network.FourSquareApiInstance
import com.example.foursquareapplication.network.PhotosApi
import com.example.foursquareapplication.network.ReviewApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PicturesDataSource(val placeId : Int) : PageKeyedDataSource<Int, PhotoData>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PhotoData>
    ) {
        val response = FourSquareApiInstance.getApiInstance(PhotosApi::class.java)
            .getPictures(placeId, FIRST_PAGE,3)
            .execute()
        if(response.body()?.getData()!=null) {

            callback.onResult(response.body()!!.getData(), null, FIRST_PAGE + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoData>) {
        val response = FourSquareApiInstance.getApiInstance(PhotosApi::class.java)
            .getPictures(placeId,params.key,3)
            .enqueue(object : Callback<Photos> {
                override fun onResponse(call: Call<Photos>, response: Response<Photos>) {
                    val adjacentKey = if (params.key > 0) params.key - 1 else null
                    if (response.body() != null) {

                        callback.onResult(response.body()!!.getData(), adjacentKey)
                    }
                }

                override fun onFailure(call: Call<Photos>, t: Throwable) {

                }

            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoData>) {
        FourSquareApiInstance.getApiInstance(PhotosApi::class.java)
            .getPictures(placeId,params.key,3)
            .enqueue(object : Callback<Photos>{
                override fun onResponse(call: Call<Photos>, response: Response<Photos>) {
                    if (response.body()?.getData() != null) {
                        //if the response has next page
                        //incrementing the next page number
                        Log.e("after","after")
                        val key = if (!(response.body()!!.getLastPage())) params.key + 1 else null

                        //passing the loaded data and next page value
                        callback.onResult(response.body()!!.getData(), key)
                    }
                }

                override fun onFailure(call: Call<Photos>, t: Throwable) {

                }

            })    }

    companion object {
        //the size of a page that we want
        const val PAGE_SIZE = 25

        //we will start from the first page which is 1
        private const val FIRST_PAGE = 0

        //we need to fetch from stackoverflow
        private const val SITE_NAME = "stackoverflow"
    }
}