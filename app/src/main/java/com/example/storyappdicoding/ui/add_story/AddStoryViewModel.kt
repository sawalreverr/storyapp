package com.example.storyappdicoding.ui.add_story

import androidx.lifecycle.ViewModel
import com.example.storyappdicoding.core.data.repository.StoryRepository
import java.io.File

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)
}