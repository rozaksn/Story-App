package com.example.storyapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keys")
data class RemoteKey(
    @PrimaryKey
    val id: String,
    val prevKey:Int?,
    val nextKey:Int?
)
