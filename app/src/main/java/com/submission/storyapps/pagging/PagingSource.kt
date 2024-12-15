package com.submission.storyapps.pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.submission.storyapps.model.Story
import com.submission.storyapps.network.ApiService
import retrofit2.HttpException
import java.io.IOException

class PagingSource(
    private val apiService: ApiService
) : PagingSource<Int, Story>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        val page = params.key ?: 1 // Halaman pertama dimulai dari 1
        return try {
            val response = apiService.getAllStories(page, params.loadSize)
            val stories = response.listStory

            LoadResult.Page(
                data = stories,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (stories.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
