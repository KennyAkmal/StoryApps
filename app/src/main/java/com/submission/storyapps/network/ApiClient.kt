package com.submission.storyapps.network

import android.content.Context
import com.submission.storyapps.BuildConfig
import com.submission.storyapps.utils.SessionManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    fun create(context: Context): ApiService {
        val sessionManager = SessionManager(context)

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = sessionManager.getToken() ?: ""
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS) // Timeout koneksi 30 detik
            .readTimeout(30, TimeUnit.SECONDS)    // Timeout membaca response 30 detik
            .writeTimeout(30, TimeUnit.SECONDS)   // Timeout menulis request 30 detik
            .retryOnConnectionFailure(true)       // Aktifkan retry otomatis
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
