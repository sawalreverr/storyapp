package com.example.storyappdicoding.core.data.local

data class UserModel(
    val userId: String,
    val token: String,
    val isLogin: Boolean = false
)