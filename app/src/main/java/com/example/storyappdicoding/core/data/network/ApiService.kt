package com.example.storyappdicoding.core.data.network

import com.example.storyappdicoding.core.data.response.login.LoginResponse
import com.example.storyappdicoding.core.data.response.register.RegisterResponse
import com.example.storyappdicoding.core.data.response.stories.AddStoryResponse
import com.example.storyappdicoding.core.data.response.stories.DetailStoryResponse
import com.example.storyappdicoding.core.data.response.stories.GetAllStoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Register
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    // Login
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    // Get All Stories
    @GET("stories")
    suspend fun stories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): GetAllStoriesResponse

    // Get all stories with location
    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): GetAllStoriesResponse

    // Get Story Detail
    @GET("stories/{id}")
    suspend fun storyDetail(
        @Path("id") id: String
    ): DetailStoryResponse

    // Add New Story
    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): AddStoryResponse
}