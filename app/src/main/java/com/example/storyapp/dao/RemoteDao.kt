package com.example.storyapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapp.entity.RemoteKey

@Dao
interface RemoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(remoteKey:List<RemoteKey>)

    @Query("SELECT * FROM keys WHERE id = :id")
     fun getRemoteKeyId(id: String): RemoteKey?

    @Query("DELETE FROM keys")
     fun deleteRemoteKey()
}
