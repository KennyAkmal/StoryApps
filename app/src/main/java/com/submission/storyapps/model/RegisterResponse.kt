package com.submission.storyapps.model

data class RegisterResponse (
    val token: String,
    val success: Boolean,
    val message: String
)