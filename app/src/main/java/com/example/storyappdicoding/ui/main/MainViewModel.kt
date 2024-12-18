package com.example.storyappdicoding.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyappdicoding.core.data.local.UserModel
import com.example.storyappdicoding.core.data.repository.StoryRepository
import com.example.storyappdicoding.core.data.response.stories.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    val getStories: LiveData<PagingData<ListStoryItem>> = repository.stories().cachedIn(viewModelScope)
}