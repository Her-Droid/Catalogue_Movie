package com.freisia.cataloguemoviedb.di.module

import com.freisia.cataloguemoviedb.data.MovieRepositoryImpl
import com.freisia.cataloguemoviedb.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module

@Module(includes = [TestNetworkModule::class,CoreModule::class])
abstract class TestRepositoryModule {
    @Binds
    abstract fun provideMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository
}