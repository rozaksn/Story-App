package com.example.storyapp.maps

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.response.StoriesResponse
import com.example.storyapp.story.StoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class MapsViewModel @Inject constructor(private val repository:StoryRepo):ViewModel(){


    fun allStoryMap(token:String):Flow<Result<StoriesResponse>> = repository.getStoryLocation(token)

    fun getToken():Flow<String?> = repository.getToken()

}