package com.example.foursquareapplication.datasource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.foursquareapplication.model.FavouriteResponse
import com.example.foursquareapplication.model.Place
import com.example.foursquareapplication.network.FavouriteApi
import com.example.foursquareapplication.network.FourSquareApiInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class GetFavouriteDataSource(val query : String,val userId: Int, val token: String) : PageKeyedDataSource<Int, Place>() {

    //this will be called once to load the initial data
    val mainPlaceList = arrayListOf<Place>()
    override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, Place>
    ) {
        try {
            val response = FourSquareApiInstance.getApiInstance(FavouriteApi::class.java)
                    .getFavourite(userId.toInt(), FIRST_PAGE,3,token).execute()

            if (response.isSuccessful) {
                if (response.body()?.getData() != null) {


                    val data = filterData(response.body()?.getData()!!)
                    callback.onResult(data, null, FIRST_PAGE + 1)
                }
            }
        } catch (e: Exception){}

    }
    fun filterData(data :List<Place>): List<Place> {
        val temp = arrayListOf<Place>()
        return if(query == ""){
            Log.d("original",data.toString())
            data

        }else{
            for(i in data){
                if(i.getName().contains(query,true)){
                    if(!temp.contains(i)) {
                        temp.add(i)
                        Log.e("here", temp.toString())
                    }
                }
            }
            temp
        }
    }


    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Place>) {
        val response = FourSquareApiInstance.getApiInstance(FavouriteApi::class.java)
                .getFavourite(userId.toInt(),params.key,3,token)
                .enqueue(object : Callback<FavouriteResponse> {
                    override fun onResponse(call: Call<FavouriteResponse>, response: Response<FavouriteResponse>) {
                        val adjacentKey = if (params.key > 0) params.key - 1 else null
                        if (response.body()?.getData() != null) {

                            val data = filterData(response.body()!!.getData())
                            callback.onResult(data, adjacentKey)
                        }
                    }

                    override fun onFailure(call: Call<FavouriteResponse>, t: Throwable) {

                    }

                })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Place>) {
        FourSquareApiInstance.getApiInstance(FavouriteApi::class.java)
                .getFavourite(userId.toInt(),params.key,3,token)
                .enqueue(object : Callback<FavouriteResponse> {
                    override fun onResponse(call: Call<FavouriteResponse>, response: Response<FavouriteResponse>) {
                        if (response.body()?.getData() != null) {
                            //if the response has next page
                            //incrementing the next page number
                            Log.e("after","after")
                            val key = if (!(response.body()!!.getLastPage())) params.key + 1 else null

                            //passing the loaded data and next page value
                            val data = filterData(response.body()!!.getData())
                            callback.onResult(data, key)
                        }
                    }

                    override fun onFailure(call: Call<FavouriteResponse>, t: Throwable) {

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