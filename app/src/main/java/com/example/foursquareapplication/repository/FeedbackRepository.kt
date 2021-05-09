package com.example.foursquareapplication.repository

import android.provider.SyncStateContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.Feedback
import com.example.foursquareapplication.model.Resource
import com.example.foursquareapplication.network.FeedbackApi
import com.example.foursquareapplication.network.FourSquareApiInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedbackRepository {
    private val feedbackApi = FourSquareApiInstance.getApiInstance(FeedbackApi::class.java)
    fun submitFeedBack(feedback : HashMap<String,String>, token : String): LiveData<Resource<Feedback>> {
        val feedbackResult = MutableLiveData<Resource<Feedback>>()
        val feedbackCall = feedbackApi.submitFeedback(feedback,token)
        feedbackResult.postValue(Resource.loading())
        feedbackCall.enqueue(object : Callback<Feedback>{
            override fun onResponse(call: Call<Feedback>, response: Response<Feedback>) {
                if(response.isSuccessful){
                    if(response.body()?.getStatus() == Constants.STATUS_OK)
                        feedbackResult.postValue(Resource.success(response.body()))
                    else
                        feedbackResult.postValue(Resource.error(response.body()?.getMessage().toString()))
                }else
                    feedbackResult.postValue(Resource.error("Couldn't submit feedback! Try again"))
            }

            override fun onFailure(call: Call<Feedback>, t: Throwable) {
                feedbackResult.postValue(Resource.error(t.message.toString()))
            }

        })
        return feedbackResult
    }
}