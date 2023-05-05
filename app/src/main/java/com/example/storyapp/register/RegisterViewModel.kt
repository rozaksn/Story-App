package com.example.storyapp.register

import android.content.ContentValues
import android.util.Log
import androidx.annotation.ContentView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.API.ApiConfig
import com.example.storyapp.response.RegisterResponse
import com.example.storyapp.user.UserModel
import com.example.storyapp.user.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel:ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun register(name:String,email:String,password:String){
        _isLoading.value = false
        val client = ApiConfig.getApiService().createAccount(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody?.message == "Akun dibuat"){
                        Log.d(TAG,responseBody?.message.toString())
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                   _isLoading.value = false
                    Log.d(TAG,t.message.toString())
                }
        })
    }
    companion object{
        const val TAG = "extar_tag"
    }
}
