package com.freisia.cataloguemoviedb.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.freisia.cataloguemoviedb.data.Resource
import com.freisia.cataloguemoviedb.domain.model.MovieDetail
import com.freisia.cataloguemoviedb.domain.usecase.MovieUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel  @Inject constructor(private val movieUseCase: MovieUseCase)
    : ViewModel() {

    private val _movieDetail = MutableLiveData<Resource<MovieDetail>>()
    val movieDetail: LiveData<Resource<MovieDetail>>
        get() = _movieDetail

    fun getReviewData(id: Int) = movieUseCase.getReviewById(id).cachedIn(viewModelScope)

    fun getDetailMovie(id: Int) {
        viewModelScope.launch {
            movieUseCase.getMovieById(id).collect {
                _movieDetail.postValue(it)
            }
        }
    }

    fun getVideoTrailerData(id: Int) = movieUseCase.getTrailerById(id).cachedIn(viewModelScope)
}