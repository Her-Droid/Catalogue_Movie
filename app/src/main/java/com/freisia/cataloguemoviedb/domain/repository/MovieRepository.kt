package com.freisia.cataloguemoviedb.domain.repository

import androidx.paging.PagingData
import com.freisia.cataloguemoviedb.data.Resource
import com.freisia.cataloguemoviedb.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovieList(genres: String?) : Flow<PagingData<Movie>>

    fun getMovieById(id: Int): Flow<Resource<MovieDetail>>

    fun getListGenres(language: String = "en-US"): Flow<Resource<List<Genre>>>

    fun getReviewById(id: Int): Flow<PagingData<Review>>

    fun getTrailerById(id: Int): Flow<PagingData<VideoTrailer>>

    fun onClear()
}