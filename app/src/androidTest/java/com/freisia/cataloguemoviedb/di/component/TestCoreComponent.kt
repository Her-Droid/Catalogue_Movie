package com.freisia.cataloguemoviedb.di.component

import android.content.Context
import com.freisia.cataloguemoviedb.di.module.TestRepositoryModule
import com.freisia.cataloguemoviedb.domain.repository.MovieRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestRepositoryModule::class])
interface TestCoreComponent : CoreComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): TestCoreComponent
    }

    override fun provideMovieRepository(): MovieRepository
}