package com.example.storyappdicoding.core.data.repository

import androidx.lifecycle.liveData
import com.example.storyappdicoding.core.data.Result
import com.example.storyappdicoding.core.data.local.UserModel
import com.example.storyappdicoding.core.data.local.UserPreference
import com.example.storyappdicoding.core.data.network.ApiService
import com.example.storyappdicoding.core.data.response.stories.AddStoryResponse
import com.example.storyappdicoding.core.data.response.stories.DetailStoryResponse
import com.example.storyappdicoding.core.data.response.stories.GetAllStoriesResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository(private val apiService: ApiService, private val pref: UserPreference) {
    // Get Session
    fun getSession(): Flow<UserModel> {
        return pref.getSession()
    }

    // Logout
    suspend fun logout() {
        pref.logout()
    }

    // List Stories
    fun stories() = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.stories()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, GetAllStoriesResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    // Detail Story
    fun detailStory(id: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.storyDetail(id)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DetailStoryResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    // New Story
    fun uploadImage(imageFile: File, description: String) = liveData {
        emit(Result.Loading)

        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData("photo", imageFile.name, requestImageFile)

        try {
            val successResponse = apiService.postStory(multipartBody, requestBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService, pref: UserPreference) =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, pref)
            }.also { instance = it }
    }
}