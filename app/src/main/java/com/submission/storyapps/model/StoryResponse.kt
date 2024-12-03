package com.submission.storyapps.model

data class StoryResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<Story>
)
