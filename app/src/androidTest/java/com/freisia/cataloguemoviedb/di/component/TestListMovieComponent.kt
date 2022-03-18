package com.freisia.cataloguemoviedb.di.component

import com.freisia.cataloguemoviedb.di.module.ListMovieViewModelModule
import com.freisia.cataloguemoviedb.di.module.UseCaseModule
import com.freisia.cataloguemoviedb.di.scope.ListMovieScope
import com.freisia.cataloguemoviedb.ui.ListFragmentTest
import dagger.Component

@ListMovieScope
@Component(
    dependencies = [TestCoreComponent::class],
    modules = [UseCaseModule::class, ListMovieViewModelModule::class]
)
interface TestListMovieComponent : ListMovieComponent{
    fun inject(fragmentTest: ListFragmentTest)
}