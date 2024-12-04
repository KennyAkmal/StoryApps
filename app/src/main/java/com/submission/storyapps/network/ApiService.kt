package com.submission.storyapps.network

import com.submission.storyapps.model.AddStoryResponse
import com.submission.storyapps.model.LoginRequest
import com.submission.storyapps.model.LoginResponse
import com.submission.storyapps.model.RegisterRequest
import com.submission.storyapps.model.RegisterResponse
import com.submission.storyapps.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("register")
    fun userRegister(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("login")
    fun userLogin(@Body request: LoginRequest): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part?
    ): Call<AddStoryResponse>


}