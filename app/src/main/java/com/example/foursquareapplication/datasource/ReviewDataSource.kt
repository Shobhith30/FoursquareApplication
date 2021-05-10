package com.example.foursquareapplication.datasource

import android.content.ClipData
import android.util.Log
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.foursquareapplication.model.Review
import com.example.foursquareapplication.model.ReviewData
import com.example.foursquareapplication.network.FourSquareApiInstance
import com.example.foursquareapplication.network.ReviewApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.Exception


class ReviewDataSource(val reviewApi: ReviewApi,val placeId : Int) : PagingSource<Int,ReviewData>() {
    //this will be called once to load the initial data



    companion object {
        //the size of a page that we want
        const val PAGE_SIZE = 10

        //we will start from the first page which is 1
        private const val FIRST_PAGE = 0

        //we need to fetch from stackoverflow
        private const val SITE_NAME = "stackoverflow"
    }

    override fun getRefreshKey(state: PagingState<Int, ReviewData>): Int? {
        return state?.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewData> {
        val position = params.key ?: 0
        return try {
            val response =
                reviewApi.getReviews(placeId, position, params.loadSize)
            val review = response.getData()
            LoadResult.Page (
                data = review?: emptyList(),
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (review==null) null else position + 1
            )
        }catch (exception : IOException){
            LoadResult.Error(exception)
        }catch (exception : HttpException){
            LoadResult.Error(exception)
        }
    }
}