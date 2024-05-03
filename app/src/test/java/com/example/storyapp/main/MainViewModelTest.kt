package com.example.storyapp.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.API.ApiService
import com.example.storyapp.CoroutineRule
import com.example.storyapp.DummyStoryData
import com.example.storyapp.PagingDataSourceTest
import com.example.storyapp.dao.StoriesDatabase
import com.example.storyapp.getOrAwaitValue
import com.example.storyapp.entity.Story
import com.example.storyapp.story.StoryAdapter
import com.example.storyapp.story.StoryRepo
import com.example.storyapp.user.UserPreference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var executorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule =CoroutineRule()

    @Mock
    private lateinit var repos:StoryRepo

    private lateinit var mainViewModel: MainViewModel
    private val tokenDummy = "token"


    @Before
    fun setUp(){mainViewModel= MainViewModel(repos = repos)}



    @Test
    fun `getListStories when data is not Null`()= runTest {
        val dummyData = DummyStoryData.dummyListStory()
        val pagingData = PagingDataSourceTest.storyItems(dummyData)

        val listStory = flowOf(pagingData)



        `when`(repos.getListStory(tokenDummy)).thenReturn(listStory)


        val actData = mainViewModel.getListStories(tokenDummy).getOrAwaitValue()
        val asyncPaging = AsyncPagingDataDiffer(diffCallback = StoryAdapter.DIFF_CALLBACK, updateCallback = storyListCallback,
            mainDispatcher = coroutineRule.testDispatcher, workerDispatcher = coroutineRule.testDispatcher)
        asyncPaging.submitData(actData)



       advanceUntilIdle()
        verify(repos).getListStory(tokenDummy)
        assertNotNull(asyncPaging.snapshot())
        assertEquals(dummyData.size,asyncPaging.snapshot().size)
        assertEquals(dummyData[0],asyncPaging.snapshot()[0])
    }

    @Test
    fun `getListStories when there are no images`() = runTest {
        val emptyData = emptyList<Story>()
        val pagingData = PagingDataSourceTest.storyItems(emptyData)
        val listStory = flowOf(pagingData)

        `when`(repos.getListStory(tokenDummy)).thenReturn(listStory)

        val actData = mainViewModel.getListStories(tokenDummy).getOrAwaitValue()
        val asyncPaging = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = storyListCallback,
            mainDispatcher = coroutineRule.testDispatcher,
            workerDispatcher = coroutineRule.testDispatcher
        )
        asyncPaging.submitData(actData)


        advanceUntilIdle()
        verify(repos).getListStory(tokenDummy)
        assertNotNull(asyncPaging.snapshot())
        assertEquals(0, asyncPaging.snapshot().size)
    }


    private val storyListCallback= object : ListUpdateCallback{
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

