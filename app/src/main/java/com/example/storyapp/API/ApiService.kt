package com.example.storyapp.API

import com.example.storyapp.response.LoginResponse
import com.example.storyapp.response.RegisterResponse
import com.example.storyapp.response.StoriesResponse
import com.example.storyapp.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

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
    //@Headers("Content-Type:application/json; charset=UTF-8")
     suspend fun getListStoriesLocation(
        @Header("Authorization") token: String,
        @Query("page")pages: Int? = null,
        @Query("size")size: Int? = null,
        @Query("location") locationStory:Int?=null
    ):StoriesResponse

   /* @GET("stories")
    fun getStoryLocation(
        @Header("Authorization") authToken: String,
        @Query("location")locationStory:Int
    ):Call<StoriesResponse>

    */

    @Multipart
    @POST("stories")
    fun uploadStories(
        @Header("Authorization") authToken: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ):Call<UploadResponse>

    @Multipart
    @POST("stories")
    suspend fun uploadStoriesLocation(
        @Header("Authorization") authToken: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: RequestBody?,
        @Part("lon") longitude: RequestBody?
    ): UploadResponse

}