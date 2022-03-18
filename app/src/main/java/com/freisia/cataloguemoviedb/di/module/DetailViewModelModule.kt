package com.freisia.cataloguemoviedb.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.freisia.cataloguemoviedb.di.annotation.ViewModelKey
import com.freisia.cataloguemoviedb.ui.detail.DetailViewModel
import com.freisia.cataloguemoviedb.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DetailViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun bindDetailViewModel(detailViewModel: DetailViewModel): ViewModel

}