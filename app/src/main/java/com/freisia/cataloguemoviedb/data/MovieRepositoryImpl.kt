package com.freisia.cataloguemoviedb.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.freisia.cataloguemoviedb.data.pagingsource.MoviePagingSource
import com.freisia.cataloguemoviedb.data.pagingsource.ReviewPagingSource
import com.freisia.cataloguemoviedb.data.pagingsource.VideoTrailerPagingSource
import com.freisia.cataloguemoviedb.data.source.RemoteDataSource
import com.freisia.cataloguemoviedb.data.source.network.ApiResponse
import com.freisia.cataloguemoviedb.domain.model.*
import com.freisia.cataloguemoviedb.domain.repository.MovieRepository
import com.freisia.cataloguemoviedb.utils.EspressoIdlingResource
import com.freisia.cataloguemoviedb.utils.toListGenreDomain
import com.freisia.cataloguemoviedb.utils.toMovieDetailDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val dispatcher: CoroutineDispatcher
) : MovieRepository {

    companion object {
        const val TMDB_PAGE_SIZE = 20
        const val TMDB_REVIEW_PAGE_SIZE = 10
    }
    
    private lateinit var dataSource: MoviePagingSource

    override fun getMovieList(genres: String?): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = TMDB_PAGE_SIZE,
                prefetchDistance = 1
            )
        ) {
            MoviePagingSource(remoteDataSource,genres).also{
                dataSource = it
            }
        }.flow.flowOn(dispatcher)
    }

    override fun getMovieById(id: Int): Flow<Resource<MovieDetail>> {
        return flow {
            // emit loading state
            emit(Resource.Loading())
            when (val response = remoteDataSource.getMovieById(id).first()) {
                is ApiResponse.Success -> emit(Resource.Success(response.data.toMovieDetailDomain()))
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                is ApiResponse.Empty -> emit(Resource.Error("Empty Data"))
            }
        }.catch { e ->
            // emit exception
            emit(Resource.Error(e.message.toString()))
        }.flowOn(dispatcher)
    }

    override fun getListGenres(language: String): Flow<Resource<List<Genre>>> {
        return flow {
            // emit loading state
            emit(Resource.Loading())
            when (val response = remoteDataSource.getListGenres(language).first()) {
                is ApiResponse.Success -> emit(Resource.Success(response.data.genreResponse.toListGenreDomain()))
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                is ApiResponse.Empty -> emit(Resource.Error("Empty Data"))
            }
        }.catch { e ->
            // emit exception
            emit(Resource.Error(e.message.toString()))
        }.flowOn(dispatcher)
    }

    override fun getReviewById(id: Int): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(
                pageSize = TMDB_REVIEW_PAGE_SIZE,
                prefetchDistance = 1
            )
        ) {
            ReviewPagingSource(remoteDataSource,id)
        }.flow.flowOn(dispatcher)
    }

    override fun getTrailerById(id: Int): Flow<PagingData<VideoTrailer>> {
        return Pager(
            config = PagingConfig(
                pageSize = TMDB_PAGE_SIZE,
                prefetchDistance = 1,
            )
        ) {
            VideoTrailerPagingSource(remoteDataSource, id)
        }.flow.flowOn(dispatcher)
    }

    override fun onClear() {
        dataSource.invalidate()
    }
}