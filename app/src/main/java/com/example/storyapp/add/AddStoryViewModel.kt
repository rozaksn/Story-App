package com.example.storyapp.add

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.response.UploadResponse
import com.example.storyapp.story.StoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class AddStoryViewModel @Inject constructor(private val repo:StoryRepo):ViewModel(){


   fun uploadStory(token:String, file:MultipartBody.Part, desc:RequestBody, lat: RequestBody?, lon: RequestBody?)
    :Flow<Result<UploadResponse>> = repo.uploadStory(token,file,desc,lat,lon)

   fun getToken():Flow<String?> = repo.getToken()
}