package com.freisia.cataloguemoviedb.di.module

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestEndpointModule{

    @Singleton
    @Provides
    fun providesUrl(): String = "http://127.0.0.1:8080"
}