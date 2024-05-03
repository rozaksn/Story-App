package com.example.storyapp

import android.content.Context
import androidx.room.Room
import com.example.storyapp.dao.RemoteDao
import com.example.storyapp.dao.StoriesDao
import com.example.storyapp.dao.StoriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
//@DisableInstallInCheck
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context:Context):StoriesDatabase{
        return Room.databaseBuilder(context,StoriesDatabase::class.java,"database").build()
    }

    @Provides
    fun provideStoriesDao(db: StoriesDatabase):StoriesDao = db.storiesDao()

    @Provides
    fun provideRemoteKeys(db:StoriesDatabase):RemoteDao=db.remoteKey()
}