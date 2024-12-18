package com.example.storyappdicoding.core.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappdicoding.core.data.repository.StoryRepository
import com.example.storyappdicoding.core.data.repository.UserRepository
import com.example.storyappdicoding.core.di.Injection
import com.example.storyappdicoding.ui.add_story.AddStoryViewModel
import com.example.storyappdicoding.ui.auth.login.LoginViewModel
import com.example.storyappdicoding.ui.auth.register.RegisterViewModel
import com.example.storyappdicoding.ui.detail_story.DetailStoryViewModel
import com.example.storyappdicoding.ui.main.MainViewModel
import com.example.storyappdicoding.ui.map.MapViewModel

class ViewModelFactory private constructor(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> {
                DetailStoryViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                MapViewModel(storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                val storyRepository = Injection.provideStoryRepository(context)
                val userRepository = Injection.provideUserRepository(context)
                instance ?: ViewModelFactory(storyRepository, userRepository)
            }.also { instance = it }
        }
    }
}