package com.example.storyapp.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.response.LoginResult
import com.example.storyapp.story.StoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@ExperimentalPagingApi
@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: StoryRepo) : ViewModel() {
    val login:LiveData<LoginResult> = repo.login
    val toastMessage:LiveData<String> = repo.toast
    val isLoading:LiveData<Boolean> = repo.isLoading

    fun login(email:String, password:String)=repo.loginRepo(email, password)



}