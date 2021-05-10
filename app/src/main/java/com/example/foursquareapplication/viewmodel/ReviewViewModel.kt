package com.example.foursquareapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import androidx.paging.PagingData
import com.example.foursquareapplication.datasource.ReviewDataSource
import com.example.foursquareapplication.model.Rating
import com.example.foursquareapplication.model.ReviewData
import com.example.foursquareapplication.repository.ReviewRepository


class ReviewViewModel(application: Application)  : AndroidViewModel(application) {

    //creating livedata for PagedList  and PagedKeyedDataSource
    val reviewRepository = ReviewRepository(application)


    fun addRating(token : String,rating : HashMap<String,String>) : LiveData<Rating>{
        return reviewRepository.addRating(token,rating)
    }

    fun getReviewData(placeId: Int) : LiveData<PagingData<ReviewData>>{
        return reviewRepository.getReviewData(placeId)
    }
}