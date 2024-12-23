package com.submission.storyapps.ui.story

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.submission.storyapps.model.Story
import com.submission.storyapps.pagging.Repo
import kotlinx.coroutines.flow.Flow

class ViewModelStory(private val repository: Repo) : ViewModel() {
    fun getStories(): Flow<PagingData<Story>> {
        return repository.getStories()
    }
}
