package com.submission.storyapps.model

data class StoryResponse(
    val listStory: List<Story>,
    val error: Boolean,
    val message: String,
    val totalPages: Int?,
    val currentPage: Int?
)

