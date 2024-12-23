package com.submission.storyapps.ui.story

import FakePagingSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.testing.asSnapshot
import com.submission.storyapps.model.Story
import com.submission.storyapps.pagging.Repo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ViewModelStoryTest {
    private lateinit var repo: Repo
    private lateinit var viewModel: ViewModelStory

    @Before
    fun setUp() {
        repo = object : Repo {
            override fun getStories() = flow {
                val mockStories = listOf(
                    Story(
                        id = "1",
                        name = "Story 1",
                        description = "Description 1",
                        createdAt = "2023-01-01T10:00:00Z",
                        lat = -7.100000,
                        lon = 106.816666,
                        photoUrl = "https://example.com/story1.jpg"
                    ),
                    Story(
                        id = "2",
                        name = "Story 2",
                        description = "Description 2",
                        createdAt = "2023-01-02T11:00:00Z",
                        lat = -6.200000,
                        lon = 106.816666,
                        photoUrl = "https://example.com/story2.jpg"
                    )
                )
                val pagingSource = FakePagingSource(mockStories)
                val pager = Pager(
                    config = PagingConfig(pageSize = 10),
                    pagingSourceFactory = { pagingSource }
                )
                emit(pager.flow.first())
            }
        }
        viewModel = ViewModelStory(repo)
    }

    @Test
    fun getStories_shouldReturnNonEmptyList() = runBlocking {
        val storiesFlow = viewModel.getStories()
        val storyList = storiesFlow.asSnapshot()
        assertNotNull(storyList)
        assertEquals(2, storyList.size)
        val firstStory = storyList[0]
        assertEquals("1", firstStory.id)
        assertEquals("Story 1", firstStory.name)
        assertEquals("Description 1", firstStory.description)
        assertEquals("2023-01-01T10:00:00Z", firstStory.createdAt)
        firstStory.lat?.let { assertEquals(-7.100000, it, 0.0) }
        firstStory.lon?.let { assertEquals(106.816666, it, 0.0) }
        assertEquals("https://example.com/story1.jpg", firstStory.photoUrl)
    }

    @Test
    fun getStories_shouldReturnEmptyListWhenNoData() = runBlocking {
        repo = object : Repo {
            override fun getStories() = flow {
                val pagingSource = FakePagingSource(emptyList())
                val pager = Pager(
                    config = PagingConfig(pageSize = 10),
                    pagingSourceFactory = { pagingSource }
                )
                emit(pager.flow.first())
            }
        }
        viewModel = ViewModelStory(repo)
        val storiesFlow = viewModel.getStories()
        val storyList = storiesFlow.asSnapshot()
        assertNotNull(storyList)
        assertEquals(0, storyList.size)
    }
}
