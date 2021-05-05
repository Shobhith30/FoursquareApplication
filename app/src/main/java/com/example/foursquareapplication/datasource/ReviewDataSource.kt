package com.example.foursquareapplication.datasource

import android.content.ClipData
import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.foursquareapplication.model.Review
import com.example.foursquareapplication.model.ReviewData
import com.example.foursquareapplication.network.FourSquareApiInstance
import com.example.foursquareapplication.network.ReviewApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReviewDataSource(val placeId : Int) : PageKeyedDataSource<Int,ReviewData>() {
    //this will be called once to load the initial data
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ReviewData>
    ) {
        val response = FourSquareApiInstance.getApiInstance(ReviewApi::class.java)
            .getReviews(placeId, FIRST_PAGE,3)
            .execute()
        if(response.body()!=null) {

            callback.onResult(response.body()!!.getData(), null, FIRST_PAGE + 1)
        }

    }

    //this will load the previous page
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ReviewData>) {
        val response = FourSquareApiInstance.getApiInstance(ReviewApi::class.java)
            .getReviews(placeId,params.key,3)
            .enqueue(object : Callback<Review>{
                override fun onResponse(call: Call<Review>, response: Response<Review>) {
                    val adjacentKey = if (params.key > 0) params.key - 1 else null
                    if (response.body() != null) {

                        callback.onResult(response.body()!!.getData(), adjacentKey)
                    }
                }

                override fun onFailure(call: Call<Review>, t: Throwable) {

                }

            })


                }



    //this will load the next page
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ReviewData>) {
        FourSquareApiInstance.getApiInstance(ReviewApi::class.java)
            .getReviews(placeId,params.key,3)
            .enqueue(object : Callback<Review>{
                override fun onResponse(call: Call<Review>, response: Response<Review>) {
                    if (response.body() != null) {
                        //if the response has next page
                        //incrementing the next page number
                        Log.e("after","after")
                        val key = if (!(response.body()!!.getLastPage())) params.key + 1 else null

                        //passing the loaded data and next page value
                        callback.onResult(response.body()!!.getData(), key)
                    }
                }

                override fun onFailure(call: Call<Review>, t: Throwable) {

                }

            })

                }



    companion object {
        //the size of a page that we want
        const val PAGE_SIZE = 3

        //we will start from the first page which is 1
        private const val FIRST_PAGE = 0

        //we need to fetch from stackoverflow
        private const val SITE_NAME = "stackoverflow"
    }
}