package com.example.storyapp.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.story.StoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository:StoryRepo) :ViewModel() {
    val isLoading:LiveData<Boolean> = repository.isLoading
    fun register(name:String,email:String,password:String){
        repository.registerRepo(name, email, password)
    }

}
