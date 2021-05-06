package com.example.foursquareapplication.datasource

import android.content.ClipData
import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.PlaceResponse
import com.example.foursquareapplication.model.Review
import com.example.foursquareapplication.model.ReviewData
import com.example.foursquareapplication.network.FourSquareApiInstance
import com.example.foursquareapplication.network.PlaceApi
import com.example.foursquareapplication.network.ReviewApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class PlaceDataSource(val type : String,val latitude : Double,val longitude : Double) : PageKeyedDataSource<Int,DataPlace>() {
    //this will be called once to load the initial data
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DataPlace>
    ) {
        try {
            val response = FourSquareApiInstance.getApiInstance(PlaceApi::class.java)
                .getPlace(type, latitude, longitude, FIRST_PAGE, PAGE_SIZE)
                .execute()
            if (response.isSuccessful) {
                if (response.body()?.getData() != null) {

                    callback.onResult(response.body()!!.getData(), null, FIRST_PAGE + 1)
                }
            }
        }catch (e:Exception){}

    }

    //this will load the previous page
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DataPlace>) {
        val response = FourSquareApiInstance.getApiInstance(PlaceApi::class.java)
            .getPlace(type,latitude,longitude, params.key ,PAGE_SIZE)
            .enqueue(object : Callback<PlaceResponse>{
                override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                    val adjacentKey = if (params.key > 0) params.key - 1 else null
                    if (response.body()?.getData() != null) {

                        callback.onResult(response.body()!!.getData(), adjacentKey)
                    }
                }

                override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {

                }

            })


    }



    //this will load the next page
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DataPlace>) {
        FourSquareApiInstance.getApiInstance(PlaceApi::class.java)
            .getPlace(type,latitude,longitude, params.key ,PAGE_SIZE)
            .enqueue(object : Callback<PlaceResponse>{
                override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                    if (response.body()?.getData() != null) {
                        //if the response has next page
                        //incrementing the next page number
                        Log.e("after","after")
                        val key = if (!(response.body()!!.getLastPage())) params.key + 1 else null

                        //passing the loaded data and next page value
                        callback.onResult(response.body()!!.getData(), key)
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

        //we need to fetch from stackoverflow
        private const val SITE_NAME = "stackoverflow"
    }
}