package com.example.storyapp.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.getOrAwaitValue
import com.example.storyapp.response.LoginResult
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    var executorRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var registerViewModel: RegisterViewModel


    @Test
    fun `register is work`() {
        val name = "jak"
        val email ="jak"
        val password = "12345678"
        val result = LoginResult("jak","jak","jak")
        val dataExpect = MutableLiveData<LoginResult>()

        dataExpect.value = result
        registerViewModel.register(name, email, password)
        verify(registerViewModel).register(name, email, password)
    }



}



