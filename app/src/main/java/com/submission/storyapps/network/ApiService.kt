package com.submission.storyapps.network

import com.submission.storyapps.model.AddStoryResponse
import com.submission.storyapps.model.LoginRequest
import com.submission.storyapps.model.LoginResponse
import com.submission.storyapps.model.RegisterRequest
import com.submission.storyapps.model.RegisterResponse
import com.submission.storyapps.model.Story
import com.submission.storyapps.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("register")
    fun userRegister(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("login")
    fun userLogin(@Body request: LoginRequest): Call<LoginResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<StoryResponse>

    @GET("stories")
    suspend fun getAllStoriesWithLocation(
        @Query("location") location: Int = 1
    ): StoryResponse

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part?,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): Call<AddStoryResponse>
}
