package com.freisia.cataloguemoviedb

import com.freisia.cataloguemoviedb.di.component.*


class TestApp : App() {
    override fun provideListComponent(): ListMovieComponent {
        return DaggerTestListMovieComponent.builder().testCoreComponent(provideCoreComponent() as TestCoreComponent).build()
    }

    override fun provideCoreComponent(): CoreComponent {
        return DaggerTestCoreComponent.factory().create(this)
    }

    override fun provideDetailComponent(): DetailComponent {
        // change into DaggerTestDetailComponent if already make & build TestDetailComponent
//        return DaggerTestDetailComponent.builder().testCoreComponent(provideCoreComponent() as TestCoreComponent).build()
        return super.provideDetailComponent()
    }
}