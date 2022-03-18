package com.freisia.cataloguemoviedb.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.freisia.cataloguemoviedb.di.annotation.ViewModelKey
import com.freisia.cataloguemoviedb.ui.list.ListViewModel
import com.freisia.cataloguemoviedb.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ListMovieViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    abstract fun bindListViewModel(listViewModel: ListViewModel): ViewModel
}