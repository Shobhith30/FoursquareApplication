package com.example.foursquareapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.foursquareapplication.model.Photos
import com.example.foursquareapplication.model.Review
import com.example.foursquareapplication.repository.AddReviewRepository
import com.example.foursquareapplication.repository.PhotosRepository

class PhotosViewModel(application: Application)  : AndroidViewModel(application) {
    private val getPhotosRepository = PhotosRepository(application)

    fun getPhotos(placeId:Int,pageNo:Int,pageSize:Int): LiveData<Photos> {

        return getPhotosRepository.getPictures(placeId,pageNo,pageSize)
    }
}