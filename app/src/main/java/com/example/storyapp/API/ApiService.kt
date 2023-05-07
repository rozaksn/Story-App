package com.example.storyapp.API

import com.example.storyapp.response.LoginResponse
import com.example.storyapp.response.RegisterResponse
import com.example.storyapp.response.StoriesResponse
import com.example.storyapp.response.UploadResponse
import com.example.storyapp.story.StoryModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import retrofit2.Call

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun createAccount(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ):Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ):Call<LoginResponse>

    @GET("stories")
    @Headers("Content-Type:application/json; charset=UTF-8")
    fun getListStories(
        @Header("Authorization") authToken: String
    ):Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun uploadStories(
        @Header("Authorization") authToken: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ):Call<UploadResponse>

}