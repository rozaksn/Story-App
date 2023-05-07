package com.example.storyapp.main

import android.util.Log
import androidx.lifecycle.*
import com.example.storyapp.API.ApiConfig
import com.example.storyapp.response.ListStoryItem
import com.example.storyapp.response.StoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel:ViewModel() {
    private val _listStories = MutableLiveData<ArrayList<ListStoryItem>>()
    val listStories: LiveData<ArrayList<ListStoryItem>> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getList(authToken: String){
        _isLoading.value = true
         val client=ApiConfig().getApiService().getListStories(authToken = "Bearer ${authToken}")
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
                }
            }


            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG,t.message.toString())
            }

        })
    }
    companion object{
        const val TAG = "extra_tag"
    }

}