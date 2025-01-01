package com.submission.storyapps.pagging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.submission.storyapps.model.Story
import com.submission.storyapps.network.ApiService
import kotlinx.coroutines.flow.Flow

class RepoImpl(private val apiService: ApiService) : Repo {
    override fun getStories(): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PagingSource(apiService) }
        ).flow
    }
}
