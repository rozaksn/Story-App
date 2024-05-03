package com.example.storyapp

import com.example.storyapp.API.ApiConfig
import com.example.storyapp.API.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideApiService():ApiService = ApiConfig().getApiService()
}