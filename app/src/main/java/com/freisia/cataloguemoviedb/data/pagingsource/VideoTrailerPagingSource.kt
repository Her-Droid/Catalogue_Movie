package com.freisia.cataloguemoviedb.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.freisia.cataloguemoviedb.data.source.RemoteDataSource
import com.freisia.cataloguemoviedb.data.source.network.ApiResponse
import com.freisia.cataloguemoviedb.domain.model.VideoTrailer
import com.freisia.cataloguemoviedb.utils.toListReviewDomain
import com.freisia.cataloguemoviedb.utils.toListVideoTrailerDomain
import kotlinx.coroutines.flow.first

class VideoTrailerPagingSource (private val remoteDataSource: RemoteDataSource, private val id: Int)
    : PagingSource<Int,VideoTrailer>() {
    override fun getRefreshKey(state: PagingState<Int, VideoTrailer>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoTrailer> {
        try {
            return when (val response = remoteDataSource.getTrailerById(id).first()) {
                is ApiResponse.Success -> {
                    LoadResult.Page(
                        data = response.data.videoResultResponse.toListVideoTrailerDomain(),
                        prevKey = null,
                        nextKey = null
                    )
                }
                is ApiResponse.Empty -> {
                    LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null
                    )
                }
                is ApiResponse.Error -> {
                    LoadResult.Error(Exception(response.errorMessage))
                }
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}