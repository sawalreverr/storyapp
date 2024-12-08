package com.example.storyappdicoding.core.data.repository

import androidx.lifecycle.liveData
import com.example.storyappdicoding.core.data.Result
import com.example.storyappdicoding.core.data.local.UserModel
import com.example.storyappdicoding.core.data.local.UserPreference
import com.example.storyappdicoding.core.data.network.ApiService
import com.example.storyappdicoding.core.data.response.login.LoginResponse
import com.example.storyappdicoding.core.data.response.register.RegisterResponse
import com.google.gson.Gson
import retrofit2.HttpException

class UserRepository(private val apiService: ApiService, private val pref: UserPreference) {
    // Register
    fun register(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    // Login
    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            response.loginResult?.let { data ->
                pref.saveSession(UserModel(data.userId!!, data.token!!))
            }
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService, pref: UserPreference) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, pref)
            }.also { instance = it }
    }
}