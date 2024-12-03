package com.submission.storyapps.network

import com.submission.storyapps.model.LoginRequest
import com.submission.storyapps.model.LoginResponse
import com.submission.storyapps.model.RegisterRequest
import com.submission.storyapps.model.RegisterResponse
import com.submission.storyapps.model.StoryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("register")
    fun userRegister(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("login")
    fun userLogin(@Body request: LoginRequest): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int = 0
    ): Call<StoryResponse>
}