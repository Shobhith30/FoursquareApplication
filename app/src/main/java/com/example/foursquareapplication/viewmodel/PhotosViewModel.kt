package com.example.foursquareapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.example.foursquareapplication.datasource.PicturesDataSource
import com.example.foursquareapplication.datasource.PicturesDataSourseFactory
import com.example.foursquareapplication.model.PhotoData
import com.example.foursquareapplication.repository.PhotosRepository

class PhotosViewModel(application: Application)  : AndroidViewModel(application) {

    //creating livedata for PagedList  and PagedKeyedDataSource
    var itemPagedList: LiveData<PagedList<PhotoData>>? = null
    var liveDataSource: LiveData<PageKeyedDataSource<Int, PhotoData>>? = null

    private val getPhotosRepository = PhotosRepository(application)

    fun getPictures(placeId : Int)  : LiveData<PagedList<PhotoData>>?{
        val itemDataSourceFactory = PicturesDataSourseFactory(placeId)

        //getting the live data source from data source factory
        liveDataSource = itemDataSourceFactory.itemLiveDataSource

        //Getting PagedList config
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false).setInitialLoadSizeHint(5)
            .setPageSize(PicturesDataSource.PAGE_SIZE).build()

        //Building the paged list
        itemPagedList = LivePagedListBuilder(itemDataSourceFactory,pagedListConfig).build()
        return itemPagedList
    }














/* private val getPhotosRepository = PhotosRepository(application)

    fun getPhotos(placeId:Int,pageNo:Int,pageSize:Int): LiveData<Photos> {

        return getPhotosRepository.getPictures(placeId,pageNo,pageSize)
    }
*/
}