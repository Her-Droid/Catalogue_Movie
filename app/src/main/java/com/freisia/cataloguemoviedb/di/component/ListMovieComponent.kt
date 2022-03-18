package com.freisia.cataloguemoviedb.di.component

import com.freisia.cataloguemoviedb.di.module.UseCaseModule
import com.freisia.cataloguemoviedb.di.module.ListMovieViewModelModule
import com.freisia.cataloguemoviedb.di.scope.ListMovieScope
import com.freisia.cataloguemoviedb.ui.list.GenreFilterDialogFragment
import com.freisia.cataloguemoviedb.ui.list.ListFragment
import dagger.Component

@ListMovieScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [UseCaseModule::class,ListMovieViewModelModule::class]
)
interface ListMovieComponent {
    @Component.Factory
    interface Factory{
        fun create(coreComponent: CoreComponent): ListMovieComponent
    }

    fun inject(fragment: ListFragment)
    fun inject(fragment: GenreFilterDialogFragment)
}