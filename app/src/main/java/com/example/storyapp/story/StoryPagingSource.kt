package com.example.storyapp.story

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.API.ApiService
import com.example.storyapp.entity.Story
import com.example.storyapp.user.UserPreference
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StoryPagingSource @Inject constructor(private val preference:UserPreference, private val apiService:ApiService):PagingSource<Int,Story>() {

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { position->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.plus(1)?:page?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val token:String = preference.getUser().first().token
            val pages = params.key ?: PAGE_INDEX
            val response = apiService.getListStoriesLocation("Bearer: $token",pages,params.loadSize)
            LoadResult.Page(data = response.listStory, prevKey = if (pages==1)null else pages-1, nextKey = if (response.listStory.isNullOrEmpty())null else pages+1)
        }
        catch (e:Exception){
            return LoadResult.Error(e)
        }
    }

    companion object{
        const val PAGE_INDEX = 1
    }

}