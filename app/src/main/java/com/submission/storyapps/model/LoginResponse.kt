package com.submission.storyapps.model

data class LoginResponse (
    val token: String,
    val success: Boolean,
    val message: String
)