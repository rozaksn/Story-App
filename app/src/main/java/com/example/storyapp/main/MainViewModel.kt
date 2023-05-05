package com.example.storyapp.main

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.storyapp.API.ApiConfig
import com.example.storyapp.response.ListStoryItem
import com.example.storyapp.response.StoriesResponse
import com.example.storyapp.user.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel:ViewModel() {
    private val _listStories = MutableLiveData<ArrayList<ListStoryItem>>()
    val listStories: LiveData<ArrayList<ListStoryItem>> = _listStories
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getList(authToken:String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListStories("Bearer ${authToken}")
        client.enqueue(object : Callback<StoriesResponse>{
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
                ) {
                _isLoading.value=false
                val responseBody=response.body()
                if (response.isSuccessful){
                    _listStories.postValue(responseBody?.listStory)
                    Log.d(TAG,responseBody?.listStory.toString())
                }else{
                    Log.e(TAG, "onFailureResponse: ${response.message()}")
                }
            }

            //@RequiresApi(Build.VERSION_CODES.R)

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG,"onFailure: ${t.message}")
            }

        })
    }


}