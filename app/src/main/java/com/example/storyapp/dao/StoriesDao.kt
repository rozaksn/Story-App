package com.example.storyapp.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapp.entity.Story

@Dao
interface StoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStories( story: Story)

    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int,Story>

    @Query("DELETE FROM story")
    fun deleteStory()
}
