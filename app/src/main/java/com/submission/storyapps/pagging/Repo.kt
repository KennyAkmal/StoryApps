package com.submission.storyapps.pagging

import androidx.paging.PagingData
import com.submission.storyapps.model.Story
import kotlinx.coroutines.flow.Flow

interface Repo {
    fun getStories(): Flow<PagingData<Story>>
}
