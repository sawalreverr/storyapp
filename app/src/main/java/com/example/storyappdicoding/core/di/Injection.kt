package com.example.storyappdicoding.core.di

import android.content.Context
import com.example.storyappdicoding.core.data.local.UserPreference
import com.example.storyappdicoding.core.data.local.dataStore
import com.example.storyappdicoding.core.data.network.ApiConfig
import com.example.storyappdicoding.core.data.repository.StoryRepository
import com.example.storyappdicoding.core.data.repository.UserRepository

object Injection {
    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return StoryRepository.getInstance(apiService, pref)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return UserRepository.getInstance(apiService, pref)
    }
}