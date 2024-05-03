package com.example.storyapp.story

import android.content.ContentValues
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.storyapp.API.ApiService
import com.example.storyapp.dao.StoriesDatabase
import com.example.storyapp.data.wrapEspressoIdle
import com.example.storyapp.entity.Story
import com.example.storyapp.response.*
import com.example.storyapp.user.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@ExperimentalPagingApi
class StoryRepo @Inject constructor(
    private val storyDataBase: StoriesDatabase,
    private val userPreference: UserPreference,
    private val apiService:ApiService,
    //private val dataStoreViewModel: DataStoreViewModels



    )
{

    private val _login = MutableLiveData<LoginResult>()
    val login:LiveData<LoginResult> = _login

    private val _isLoading=MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _toast = MutableLiveData<String>()
    val toast:LiveData<String> = _toast




    //menampilkan list story


    fun getListStory(token:String): Flow<PagingData<Story>>{


        return Pager(
            config = PagingConfig(pageSize = 10), remoteMediator = StoryRemoteMediator(storyDataBase,authTokenGenerator(token),apiService),
        pagingSourceFactory = {storyDataBase.storiesDao().getStories() }
        ).flow

    }



    fun getStoryLocation(token:String):Flow<Result<StoriesResponse>> = flow{
        wrapEspressoIdle{
           try {
                val authToken = authTokenGenerator(token)
                val apiResponse = apiService.getListStoriesLocation(authToken, size = 30, locationStory = 10)
                emit(Result.success(apiResponse))
            }catch (exception:Exception){
                exception.printStackTrace()
                emit(Result.failure(exception))
            }


        }


    }

    private fun authTokenGenerator(token:String):String{
        return "Bearer ${token}"
    }

    //login test
    fun loginRepo(email:String,password:String){
        wrapEspressoIdle {
            _isLoading.value = true
            apiService.login(email, password).enqueue(
                object : Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        _isLoading.value = false
                        val responseBody = response.body()
                        if (response.isSuccessful){
                            _toast.value = responseBody?.message
                            _login.value = responseBody?.loginResult

                            Log.d(ContentValues.TAG, response.body()?.message.toString())
                            Log.d(ContentValues.TAG, response.body()?.loginResult?.token.toString())
                            Log.d(ContentValues.TAG, response.body()?.loginResult?.name ?: "name")
                            Log.d(ContentValues.TAG, response.body()?.loginResult?.userId ?: "userId")
                        }

                        if (!response.isSuccessful){
                            _toast.value =response.message()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        _toast.value = t.message
                        _isLoading.value = false
                        Log.d(TAG,t.message.toString())
                    }

                }
            )
        }

    }

    //register test
    fun registerRepo(name:String, email:String, password: String){
        wrapEspressoIdle {
            _isLoading.value = true
            apiService.createAccount(name, email, password).enqueue(
                object:Callback<RegisterResponse>{
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        _isLoading.value = false
                        val responseBody = response.body()
                        if (response.isSuccessful){
                            Log.d(TAG,responseBody?.message.toString())
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        _isLoading.value = false
                        Log.d(TAG,t.message.toString())
                    }

                }
            )
        }
    }

    fun uploadStory(token: String, photo:MultipartBody.Part, description:RequestBody,lat:RequestBody?=null,lon:RequestBody?=null)
    :Flow<Result<UploadResponse>> = flow {
        try {

            val authToken = authTokenGenerator(token)
            val apiResponse = apiService.uploadStoriesLocation(authToken,photo,description,lat,lon)
            emit(Result.success(apiResponse))
        }catch (exception:Exception){
            exception.printStackTrace()
            emit(Result.failure(exception))
        }

    }

    fun getToken():Flow<String?> = userPreference.getToken()

    companion object{
        const val TAG = "extra_tag"
    }
}