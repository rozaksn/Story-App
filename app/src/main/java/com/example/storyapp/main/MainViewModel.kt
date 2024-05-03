package com.example.storyapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.entity.Story
import com.example.storyapp.story.StoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class MainViewModel @Inject constructor(private val repos: StoryRepo) :ViewModel() {

    fun getListStories(token:String):LiveData<PagingData<Story>> = repos.getListStory(token)
        .cachedIn(viewModelScope).asLiveData()

    fun getToken(): Flow<String?> = repos.getToken()


}