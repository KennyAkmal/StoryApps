package com.submission.storyapps.ui.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.submission.storyapps.pagging.Repo

class ViewModelStoryFactory(private val repository: Repo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelStory::class.java)) {
            return ViewModelStory(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
