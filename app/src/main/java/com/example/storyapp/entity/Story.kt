package com.example.storyapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story")

data class Story(
 @PrimaryKey
 val photoUrl: String,

 val createdAt: String,

 val name: String,

 val description: String,

 val lon: Double,

 val id: String,

 val lat: Double
)
