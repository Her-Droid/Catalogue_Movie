package com.freisia.cataloguemoviedb.di.module

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class CoreModule {

    @Singleton
    @Provides
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}