package com.freisia.cataloguemoviedb.di.module

import com.freisia.cataloguemoviedb.domain.usecase.MovieInteractor
import com.freisia.cataloguemoviedb.domain.usecase.MovieUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class UseCaseModule {
    @Binds
    abstract fun providesListMovieUseCase(listMovieInteractor: MovieInteractor) : MovieUseCase
}