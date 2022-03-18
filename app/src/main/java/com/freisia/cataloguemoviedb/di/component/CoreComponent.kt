package com.freisia.cataloguemoviedb.di.component

import android.content.Context
import com.freisia.cataloguemoviedb.di.module.RepositoryModule
import com.freisia.cataloguemoviedb.domain.repository.MovieRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class])
interface CoreComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): CoreComponent
    }

    fun provideMovieRepository(): MovieRepository

}