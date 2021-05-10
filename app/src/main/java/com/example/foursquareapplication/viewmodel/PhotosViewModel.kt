package com.example.foursquareapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import androidx.paging.PagingData
import com.example.foursquareapplication.datasource.PicturesDataSource
import com.example.foursquareapplication.model.*
import com.example.foursquareapplication.repository.PhotosRepository

class PhotosViewModel(application: Application)  : AndroidViewModel(application) {


    private val photosRepository = PhotosRepository(application)

    fun getPictureDetails(token: String?, photoId: Int): LiveData<PhotoDetails> {
        return photosRepository.getPhotoDetails(token,photoId)
    }

    fun getPicture(placeId: Int): LiveData<PagingData<PhotoData>> {

        return photosRepository.getPhotos(placeId)
    }
















/* private val getPhotosRepository = PhotosRepository(application)

    fun getPhotos(placeId:Int,pageNo:Int,pageSize:Int): LiveData<Photos> {

        return getPhotosRepository.getPictures(placeId,pageNo,pageSize)
    }
*/
}