package com.example.storyapp.story

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.storyapp.API.ApiService
import com.example.storyapp.dao.StoriesDatabase
import com.example.storyapp.data.wrapEspressoIdle
import com.example.storyapp.entity.RemoteKey
import com.example.storyapp.entity.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator @Inject constructor(
    private val dataBase: StoriesDatabase,
    private var token:String, //private val userPreference: UserPreference,
    private val apiService: ApiService

): RemoteMediator<Int,Story>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Story>
    ): MediatorResult {
        val pages = when(loadType){
            LoadType.REFRESH->{
                val remoteKey = getRemoteKeysPositions(state)
                remoteKey?.nextKey?.minus(1)?: PAGE_INDEX
            }
            LoadType.PREPEND ->{
                val remoteKey = getRemoteKeyFirst(state)
                val prevKeys = remoteKey?.prevKey ?: return  MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                prevKeys
            }
            LoadType.APPEND ->{
                val remoteKey = getRemoteKeyLAst(state)
                val nextKeys = remoteKey?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                nextKeys
            }

        }
        wrapEspressoIdle {
            try {
                //val token:String = userPreference.getUser().first().token
                val response = apiService.getListStoriesLocation(token,pages,state.config.pageSize)
                val endPage = response.listStory.isEmpty()

                dataBase.withTransaction {
                    if (loadType==LoadType.REFRESH){
                        dataBase.remoteKey().deleteRemoteKey()
                        dataBase.storiesDao().deleteStory()
                    }

                    val prevKeys = if (pages==1)null else pages-1
                    val nextKeys = if (endPage)null else pages+1
                    val key = response.listStory.map {
                        RemoteKey(id = it.id, prevKey = prevKeys, nextKey = nextKeys)
                    }
                    dataBase.remoteKey().insert(key)

                    response.listStory.forEach { storyItem->
                        val story = Story(
                            storyItem.photoUrl,storyItem.createdAt,storyItem.name,storyItem.description,
                            storyItem.lon,storyItem.id,storyItem.lat
                        )
                        dataBase.storiesDao().insertStories(story)
                    }


                }
                return MediatorResult.Success(endOfPaginationReached = endPage)
            }catch (e:Exception){
                return MediatorResult.Error(e)
            }
        }

    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private  fun getRemoteKeysPositions(pagingState: PagingState<Int, Story>): RemoteKey?{
        return pagingState.anchorPosition?.let { position->
            pagingState.closestItemToPosition(position)?.id?.let {id ->
                dataBase.remoteKey().getRemoteKeyId(id)
            }
        }

    }
    private suspend fun getRemoteKeyFirst(pagingState: PagingState<Int, Story>): RemoteKey? {
        return withContext(Dispatchers.IO) {
            pagingState.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
                dataBase.remoteKey().getRemoteKeyId(data.id)
            }
        }
    }


    private suspend fun getRemoteKeyLAst(pagingState:PagingState<Int,Story>):RemoteKey?{
        return withContext(Dispatchers.IO){
            pagingState.pages.lastOrNull{it.data.isNotEmpty() }?.data?.lastOrNull()?.let {data ->
                dataBase.remoteKey().getRemoteKeyId(data.id)}
        }
    }


    companion object{
        const val PAGE_INDEX = 1
    }
}