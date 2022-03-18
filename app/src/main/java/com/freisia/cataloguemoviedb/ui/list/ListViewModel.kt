package com.freisia.cataloguemoviedb.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.freisia.cataloguemoviedb.di.scope.ListMovieScope
import com.freisia.cataloguemoviedb.domain.usecase.MovieUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@ListMovieScope
class ListViewModel @Inject constructor(private val useCase: MovieUseCase) : ViewModel() {

    private val _genres = MutableStateFlow<String?>(null)
    val genres: StateFlow<String?> = _genres

    @OptIn(ExperimentalCoroutinesApi::class)
    var data = _genres.flatMapLatest { genre ->
        useCase.getMovieList(genre).onStart { emit(PagingData.empty()) }.cachedIn(viewModelScope)
    }

    val genreData = useCase.getListGenres()

    fun setGenre(genre: String?){
        _genres.value = genre
    }

    fun onClear(){
        useCase.onClear()
    }
}