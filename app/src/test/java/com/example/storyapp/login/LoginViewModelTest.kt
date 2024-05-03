package com.example.storyapp.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.DummyStoryData
import com.example.storyapp.getOrAwaitValue
import com.example.storyapp.entity.Story
import com.example.storyapp.response.LoginResult
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var rule=InstantTaskExecutorRule()

    @Mock
    private lateinit var loginViewModel:LoginViewModel

    @Test
    fun `login data return to right data`():Unit= runTest {
        val result = LoginResult(name = "name", userId = "userId", token = "token")
        val dataExpect = MutableLiveData<LoginResult>()

        dataExpect.value = result
        `when`(loginViewModel.login).thenReturn(dataExpect)
        val data = loginViewModel.login.getOrAwaitValue()

        Mockito.verify(loginViewModel).login
        Truth.assertThat(data).isEqualTo(dataExpect.value)
    }

    @Test
    fun `login is works normal`(){
        val email ="jak"
        val password ="12345678"
        val dummyStory = DummyStoryData.dummyListStory()
        val dataExpect = MutableLiveData<List<Story>>()

        loginViewModel.login(email, password)
        dataExpect.value = dummyStory
        Mockito.verify(loginViewModel).login(email, password)
    }


}