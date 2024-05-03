package com.example.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.user.UserModel
import com.example.storyapp.user.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@ExperimentalPagingApi
@HiltViewModel
class DataStoreViewModels @Inject constructor(private val userPreference:UserPreference):ViewModel() {
    fun getUserModel(): LiveData<UserModel>{
        return userPreference.getUser().asLiveData()
    }

    fun setUserModel(user:UserModel){
        viewModelScope.launch { userPreference.setUser(user) }
    }

    fun userLogout(){
        viewModelScope.launch { userPreference.logout() }
    }
}