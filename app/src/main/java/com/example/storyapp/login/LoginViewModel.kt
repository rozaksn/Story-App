package com.example.storyapp.login

import android.content.ContentValues.*
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.API.ApiConfig
import com.example.storyapp.response.LoginResponse
import com.example.storyapp.response.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val _login = MutableLiveData<LoginResult>()
    val login: LiveData<LoginResult> = _login

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun login(email:String, password:String){
        _isLoading.value = true
        val client = ApiConfig().getApiService().login(email,password)
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                Log.d(TAG, "onRespone: ${responseBody}")
                if (response.isSuccessful){
                    _login.value = responseBody?.loginResult

                    Log.d(TAG, response.body()?.message.toString())
                    Log.d(TAG, response.body()?.loginResult?.token.toString())
                    Log.d(TAG, response.body()?.loginResult?.name ?: "name")
                    Log.d(TAG, response.body()?.loginResult?.userId ?: "userId")
                }
                if (!response.isSuccessful) {
                    _toastMessage.value = response.message()
                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _toastMessage.value = t.message
                _isLoading.value = false
                Log.d(TAG,"onFailure: ${t.message}")
            }
        })
    }




}