package com.example.storyappdicoding.ui.detail_story

import androidx.lifecycle.ViewModel
import com.example.storyappdicoding.core.data.repository.StoryRepository

class DetailStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getStoryDetail(id: String) = repository.detailStory(id)
}