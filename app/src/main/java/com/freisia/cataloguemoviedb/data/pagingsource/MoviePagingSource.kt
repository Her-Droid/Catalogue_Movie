package com.freisia.cataloguemoviedb.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.freisia.cataloguemoviedb.data.source.RemoteDataSource
import com.freisia.cataloguemoviedb.data.source.network.ApiResponse
import com.freisia.cataloguemoviedb.domain.model.Movie
import com.freisia.cataloguemoviedb.utils.toListMovieDomain
import kotlinx.coroutines.flow.first

class MoviePagingSource(private val remoteDataSource: RemoteDataSource,private val genres: String?) :
    PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        try {
            val currentPage = params.key ?: STARTING_PAGE
            when (val response = remoteDataSource.getMovieList(currentPage,genres = genres).first()) {
                is ApiResponse.Success -> {
                    return LoadResult.Page(
                        data = response.data.listData.toListMovieDomain(),
                        prevKey = if (currentPage == STARTING_PAGE) null else currentPage.minus(
                            1
                        ),
                        nextKey = if (response.data.page >= response.data.totalPage) null else currentPage.plus(
                            1
                        )
                    )
                }
                is ApiResponse.Empty -> {
                    return LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null
                    )
                }
                is ApiResponse.Error -> {
                    return LoadResult.Error(Exception(response.errorMessage))
                }
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val STARTING_PAGE = 1
    }

}
