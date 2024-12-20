package com.example.storyappdicoding.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyappdicoding.core.data.network.ApiService
import com.example.storyappdicoding.core.data.response.stories.ListStoryItem

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, ListStoryItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.stories(position, params.loadSize)
            LoadResult.Page(
                data = responseData.listStory?.filterNotNull() ?: emptyList(),
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}