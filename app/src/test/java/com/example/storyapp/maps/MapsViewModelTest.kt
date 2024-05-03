package com.example.storyapp.maps

import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.CoroutineRule
import com.example.storyapp.DummyStoryData
import com.example.storyapp.getOrAwaitValue
import com.example.storyapp.entity.Story
import com.example.storyapp.response.LoginResult
import com.example.storyapp.response.StoriesResponse
import com.example.storyapp.story.StoryRepo
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    //@get:Rule
    //var coroutineRule = CoroutineRule()

    @Mock
    private lateinit var repo:StoryRepo
    private lateinit var mapsViewModel: MapsViewModel


    private val dummyResponse = DummyStoryData.dummyDataResponse()
    private val dummyToken = "token"

    @Before
    fun setupViewModel(){mapsViewModel= MapsViewModel(repo)}

    @Test
    fun `when getListStory success`():Unit= runTest {
       val responseExpect = flowOf(Result.success(dummyResponse))
        `when`(mapsViewModel.allStoryMap(dummyToken)).thenReturn(responseExpect)
        mapsViewModel.allStoryMap(dummyToken).collect{response->
            assertTrue(response.isSuccess)
            assertFalse(response.isFailure)
            response.onSuccess { response->
                assertNotNull(response)
                assertSame(response,dummyResponse)
            }
        }
        verify(repo).getStoryLocation(dummyToken)
    }

    @Test
    fun `when getListStory failed`():Unit= runTest {
        val responseExpect:Flow<Result<StoriesResponse>> = flowOf(Result.failure(Exception("failed")))
        `when`(mapsViewModel.allStoryMap(dummyToken)).thenReturn(responseExpect)
        mapsViewModel.allStoryMap(dummyToken).collect{response->
            assertTrue(response.isFailure)
            assertFalse(response.isSuccess)
            response.onFailure {
                assertNotNull(it)
            }
        }
    }

}

