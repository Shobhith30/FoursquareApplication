package com.example.foursquareapplication.datasource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.foursquareapplication.model.PhotoData
import com.example.foursquareapplication.model.Photos
import com.example.foursquareapplication.model.Review
import com.example.foursquareapplication.network.FourSquareApiInstance
import com.example.foursquareapplication.network.PhotosApi
import com.example.foursquareapplication.network.ReviewApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class PicturesDataSource(val photosApi : PhotosApi ,val placeId : Int) : PagingSource<Int, PhotoData>() {


    companion object {
        //the size of a page that we want
        const val PAGE_SIZE = 25
    }

    override fun getRefreshKey(state: PagingState<Int, PhotoData>): Int? {
        return state?.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoData> {
        val position = params.key ?: 0
        return try {
            val response =
                photosApi.getPictures(placeId, position, params.loadSize)
            val photos = response.getData()
            LoadResult.Page (
                data = photos?: emptyList(),
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (photos==null) null else position + 1
            )
        }catch (exception : IOException){
            LoadResult.Error(exception)
        }catch (exception : HttpException){
            LoadResult.Error(exception)
        }
    }
}