package com.freisia.cataloguemoviedb.di.component

import com.freisia.cataloguemoviedb.di.module.DetailViewModelModule
import com.freisia.cataloguemoviedb.di.module.UseCaseModule
import com.freisia.cataloguemoviedb.di.scope.DetailScope
import com.freisia.cataloguemoviedb.ui.DetailFragmentTest
import dagger.Component

@DetailScope
@Component(
    dependencies = [TestCoreComponent::class],
    modules = [UseCaseModule::class, DetailViewModelModule::class]
)
interface TestDetailComponent : DetailComponent{
    fun inject(fragmentTest:DetailFragmentTest)
}