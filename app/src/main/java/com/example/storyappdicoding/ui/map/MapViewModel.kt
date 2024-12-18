package com.example.storyappdicoding.ui.map

import androidx.lifecycle.ViewModel
import com.example.storyappdicoding.core.data.repository.StoryRepository

class MapViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getStoriesWithLocation() = repository.storiesWithLocation()
}