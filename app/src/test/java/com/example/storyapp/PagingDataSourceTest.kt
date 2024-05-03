package com.example.storyapp

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.entity.Story
import com.example.storyapp.response.ListStoryItem

class PagingDataSourceTest private constructor():PagingSource<Int,LiveData<List<Story>>>(){
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(),0,1)
    }

    companion object{
        fun storyItems(listStory:List<Story>):PagingData<Story>{
            return PagingData.from(listStory)
        }
    }

}