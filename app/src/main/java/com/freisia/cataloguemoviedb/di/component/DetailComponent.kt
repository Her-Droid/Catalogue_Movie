package com.freisia.cataloguemoviedb.di.component

import com.freisia.cataloguemoviedb.di.module.DetailViewModelModule
import com.freisia.cataloguemoviedb.di.module.UseCaseModule
import com.freisia.cataloguemoviedb.di.scope.DetailScope
import com.freisia.cataloguemoviedb.ui.detail.DetailFragment
import dagger.Component

@DetailScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [UseCaseModule::class, DetailViewModelModule::class]
)
interface DetailComponent {
    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): DetailComponent
    }

    fun inject(fragment: DetailFragment)

}