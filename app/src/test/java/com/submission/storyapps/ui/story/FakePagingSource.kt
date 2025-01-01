import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.submission.storyapps.model.Story

class FakePagingSource(private val data: List<Story>) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? = null
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return LoadResult.Page(
            data = data,
            prevKey = null,
            nextKey = null
        )
    }
}
