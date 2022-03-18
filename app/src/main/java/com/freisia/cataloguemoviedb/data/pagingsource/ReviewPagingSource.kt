package com.freisia.cataloguemoviedb.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.freisia.cataloguemoviedb.data.source.RemoteDataSource
import com.freisia.cataloguemoviedb.data.source.network.ApiResponse
import com.freisia.cataloguemoviedb.domain.model.Review
import com.freisia.cataloguemoviedb.utils.toListReviewDomain
import kotlinx.coroutines.flow.first

class ReviewPagingSource(private val remoteDataSource: RemoteDataSource, private val id: Int)
    : PagingSource<Int,Review>() {

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        try {
            val currentPage = params.key ?: STARTING_PAGE
            return when (val response = remoteDataSource.getReviewById(id,currentPage).first()) {
                is ApiResponse.Success -> {
                    LoadResult.Page(
                        data = response.data.listData.toListReviewDomain(),
                        prevKey = if (currentPage == MoviePagingSource.STARTING_PAGE) null else currentPage.minus(
                            1
                        ),
                        nextKey = if (response.data.page >= response.data.totalPage) null else currentPage.plus(
                            1
                        )
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

    companion object {
        const val STARTING_PAGE = 1
    }
}