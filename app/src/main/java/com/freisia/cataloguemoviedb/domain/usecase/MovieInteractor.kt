package com.freisia.cataloguemoviedb.domain.usecase

import androidx.paging.PagingData
import com.freisia.cataloguemoviedb.data.Resource
import com.freisia.cataloguemoviedb.domain.model.*
import com.freisia.cataloguemoviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieInteractor @Inject constructor(private val movieRepository: MovieRepository): MovieUseCase {
    override fun getMovieList(genres: String?): Flow<PagingData<Movie>> = movieRepository.getMovieList(genres)
    override fun getListGenres(language: String): Flow<Resource<List<Genre>>> = movieRepository.getListGenres(language)
    override fun getMovieById(id: Int): Flow<Resource<MovieDetail>> = movieRepository.getMovieById(id)
    override fun getReviewById(id: Int): Flow<PagingData<Review>> = movieRepository.getReviewById(id)
    override fun getTrailerById(id: Int): Flow<PagingData<VideoTrailer>> = movieRepository.getTrailerById(id)
    override fun onClear() {
        movieRepository.onClear()
    }
}