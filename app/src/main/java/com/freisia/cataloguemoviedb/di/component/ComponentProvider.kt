package com.freisia.cataloguemoviedb.di.component

interface ComponentProvider {
    fun provideListComponent(): ListMovieComponent
    fun provideDetailComponent(): DetailComponent
    fun provideCoreComponent(): CoreComponent
}