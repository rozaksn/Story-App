package com.example.storyapp

import androidx.lifecycle.MutableLiveData
import com.example.storyapp.entity.Story
import com.example.storyapp.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DummyStoryData {
    fun dummyListStory():List<Story>{
        val storyItems = arrayListOf<Story>()
        for (i in 0 .. 100){
            val listStory = Story(createdAt = "2023", photoUrl = "photo_url", name = "jak", description = "desc", id = "story_id", lon = -7.8, lat = -112.5)
            storyItems.add(listStory)
        }
        return storyItems
    }

    fun dummyDataResponse(): StoriesResponse {
        val storyItems = mutableListOf<Story>()
        val error = false
        val message = "success"
        for (i in 0..100){
            val listStory = Story("photoUrl","2023","name","desc",-7.7,"id",112.112)
            storyItems.add(listStory)
        }
        return StoriesResponse(storyItems,error, message)
    }

    fun dummyLogin():LoginResponse{
        val result=LoginResult("name","id","authToken")
        return LoginResponse(result,false,"Login success")
    }

    fun dummyRegister():RegisterResponse{
        return RegisterResponse(false,"Register success")
    }

    fun dummyAddStory():UploadResponse{
        return UploadResponse(false,"Upload success")
    }

    fun dummyMultiPart():MultipartBody.Part{
        val textDummy = "text".toRequestBody()
        return MultipartBody.Part.create(textDummy)
    }

    fun dummyRequestBody():RequestBody{
        val textDummy = "text".toRequestBody()
        return textDummy
    }
}