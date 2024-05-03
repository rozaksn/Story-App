package com.example.storyapp.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storyapp.entity.RemoteKey
import com.example.storyapp.entity.Story

/*
@DisableInstallInCheck
@Module
object DaoModule {
    @Provides
    @Singleton
    fun provideStoriesDao(database: StoriesDatabase): StoriesDao {
        return database.storiesDao()
    }

    @Provides
    @Singleton
    fun provideRemoteDao(database: StoriesDatabase): RemoteDao {
        return database.remoteKey()
    }
}

 */
//@InstallIn(ApplicationComponentManager::class)
@Database(entities = [Story::class, RemoteKey::class], version = 1, exportSchema = false)
abstract class  StoriesDatabase:RoomDatabase() {
    //@Provides
   // @Binds
    abstract fun storiesDao(): StoriesDao
    //@Binds
    abstract fun remoteKey(): RemoteDao
}

