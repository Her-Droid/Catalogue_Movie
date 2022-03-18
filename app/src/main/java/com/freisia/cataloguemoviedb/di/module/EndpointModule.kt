package com.freisia.cataloguemoviedb.di.module

import com.freisia.cataloguemoviedb.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class EndpointModule {

    @Singleton
    @Provides
    open fun providesUrl(): String = BuildConfig.BASE_URL
}