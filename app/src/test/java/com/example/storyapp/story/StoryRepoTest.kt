package com.example.storyapp.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.API.ApiService
import com.example.storyapp.CoroutineRule
import com.example.storyapp.DummyStoryData
import com.example.storyapp.PagingDataSourceTest
import com.example.storyapp.dao.StoriesDatabase
import com.example.storyapp.getOrAwaitValue
import com.example.storyapp.response.LoginResult
import com.example.storyapp.response.StoriesResponse
import com.example.storyapp.user.UserPreference
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepoTest {

    @get:Rule
    var executorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = CoroutineRule()


    @Mock
    private lateinit var database: StoriesDatabase

    @Mock
    private lateinit var apiService:ApiService

    @Mock
    private lateinit var preference:UserPreference

    @Mock
    private lateinit var storyRepoMock:StoryRepo

    @Mock
    private lateinit var storyRepo:StoryRepo



    private val dummyToken = "token"
    private val dummyFile = DummyStoryData.dummyMultiPart()
    private val dummyDesc = DummyStoryData.dummyRequestBody()
    private val dummyResponse = DummyStoryData.dummyDataResponse()

    @Before
    fun repoSetup(){
        storyRepo = StoryRepo(database,preference,apiService)
    }

    @Test
    fun getLogin()= runTest {
        val dummyResult = LoginResult("jak","jak","jak")
        val dataExpect = MutableLiveData<LoginResult>()
        dataExpect.value = dummyResult

        `when`(storyRepoMock.login).thenReturn(dataExpect)

        val actData = storyRepoMock.login.getOrAwaitValue()

        verify(storyRepoMock).login
        assertThat(actData).isEqualTo(dataExpect.value)
    }

    @Test
    fun testGetListStory()= runTest {
        val storyDummy = DummyStoryData.dummyListStory()
        val pagingData = PagingDataSourceTest.storyItems(storyDummy)
        val dummyResult = flowOf(pagingData)

        `when`(storyRepoMock.getListStory(dummyToken)).thenReturn(dummyResult)

        storyRepoMock.getListStory(dummyToken).collect{actResult->
            val asyncData=AsyncPagingDataDiffer(diffCallback = StoryAdapter.DIFF_CALLBACK, updateCallback = listUpdtCallback,
                mainDispatcher = coroutineRule.testDispatcher, workerDispatcher = coroutineRule.testDispatcher)
            asyncData.submitData(actResult)

            assertNotNull(asyncData.snapshot())
            assertEquals(dummyResponse.listStory.size,asyncData.snapshot().size)
        }

    }

    @Test
    fun `getStoryLocation success`()= runTest {

        val dataExpect = flowOf(Result.success(dummyResponse))

        `when`(storyRepoMock.getStoryLocation(dummyToken)).thenReturn(dataExpect)
        storyRepoMock.getStoryLocation(dummyToken).collect{responseResult->
            assertTrue(responseResult.isSuccess)
            assertFalse(responseResult.isFailure)
            responseResult.onSuccess { actResponse->
                assertNotNull(actResponse)
                assertEquals(dummyResponse,actResponse)
            }
        }
    }

    @Test
    fun `getStoryLocation failed`()= runTest {

        val dataExpect = flowOf<Result<StoriesResponse>>(Result.failure(Exception("failed")))

        `when`(storyRepoMock.getStoryLocation(dummyToken)).thenReturn(dataExpect)
        storyRepoMock.getStoryLocation(dummyToken).collect{responseResult->
            assertFalse(responseResult.isSuccess)
            assertTrue(responseResult.isFailure)
            responseResult.onFailure {
                assertNotNull(it)

            }
        }
    }

    @Test
    fun `loginRepo work normal`() {
        val result=LoginResult("jak","jak","jak")
        val dataExpect = MutableLiveData<LoginResult>()
        dataExpect.value = result

        storyRepoMock.loginRepo("jak", "jak")
        verify(storyRepoMock).loginRepo("jak", "jak")
        `when`(storyRepoMock.login).thenReturn(dataExpect)

        val actData = storyRepoMock.login.getOrAwaitValue()
        verify(storyRepoMock).login
        assertThat(actData).isEqualTo(dataExpect.value)
    }

    @Test
    fun `registerRepo work normal`() {
        val dataExpect = MutableLiveData<Boolean>()
        dataExpect.value = true

        storyRepoMock.registerRepo("jak","jak" ,"jak" )
        verify(storyRepoMock).registerRepo("jak", "jak", "jak")

        `when`(storyRepoMock.isLoading).thenReturn(dataExpect)

        val actData = storyRepoMock.isLoading.getOrAwaitValue()
        verify(storyRepoMock).isLoading
        assertThat(actData).isEqualTo(dataExpect.value)

    }

    @Test
    fun `uploadStory success`()= runTest {
        val dataExpect = DummyStoryData.dummyAddStory()

        `when`(apiService.uploadStoriesLocation(dummyToken.tokenGenerator(),dummyFile,dummyDesc,latitude = null,longitude = null))
            .thenReturn(dataExpect)

        storyRepo.uploadStory(dummyToken,dummyFile,dummyDesc,lat = null,lon = null).collect{responseResult->
            assertTrue(responseResult.isSuccess)
            assertFalse(responseResult.isFailure)
            responseResult.onSuccess { actData->
                assertEquals(dataExpect,actData)
            }
        }
        verify(apiService).uploadStoriesLocation(dummyToken.tokenGenerator(),dummyFile,dummyDesc,latitude = null,longitude = null)

    }

    @Test
    fun `uploadStory failed`()= runTest {

        `when`(apiService.uploadStoriesLocation(dummyToken.tokenGenerator(),dummyFile,dummyDesc,latitude = null,longitude = null))
            .then { throw Exception() }

        storyRepo.uploadStory(dummyToken,dummyFile,dummyDesc,lat = null,lon = null).collect{responseResult->
            assertTrue(responseResult.isFailure)
            assertFalse(responseResult.isSuccess)
            responseResult.onFailure {
                assertNotNull(it)
            }
        }
        verify(apiService).uploadStoriesLocation(dummyToken.tokenGenerator(),dummyFile,dummyDesc,latitude = null,longitude = null)

    }



    private fun String.tokenGenerator():String{return "Bearer $this"}

     private val listUpdtCallback = object :ListUpdateCallback{
        override fun onInserted(position: Int, count: Int) {
        }

        override fun onRemoved(position: Int, count: Int) {
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
        }

    }
}

