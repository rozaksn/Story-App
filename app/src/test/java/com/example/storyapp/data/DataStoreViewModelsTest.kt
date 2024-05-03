package com.example.storyapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.getOrAwaitValue
import com.example.storyapp.user.UserModel
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class DataStoreViewModelsTest {
    @get:Rule
    var executorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataStoreViewModel:DataStoreViewModels


    @Test
    fun `getUserModel is not null and success`() {
        val name = "jak"
        val token = "jak"
        val id = "jak"
        val state = true

        val dummyModel=UserModel(name,token,id,state)
        val dataExpect = MutableLiveData<UserModel>()
        dataExpect.value=dummyModel

        `when`(dataStoreViewModel.getUserModel()).thenReturn(dataExpect)
        val actData = dataStoreViewModel.getUserModel().getOrAwaitValue()

        verify(dataStoreViewModel).getUserModel()
        Truth.assertThat(actData).isNotNull()
        Truth.assertThat(dataExpect.value).isEqualTo(actData)
    }

    @Test
    fun `setUserModel is not null`() {
        val name = "jak"
        val token = "jak"
        val id = "jak"
        val state = true

        val dummyModel=UserModel(name,token,id,state)
        val dataExpect = dataStoreViewModel.setUserModel(dummyModel)
        verify(dataStoreViewModel).setUserModel(dummyModel)
        Truth.assertThat(dataExpect).isNotNull()

    }

    @Test
    fun `userLogout is working normal`() {
        dataStoreViewModel.userLogout()
        verify(dataStoreViewModel).userLogout()
    }
}