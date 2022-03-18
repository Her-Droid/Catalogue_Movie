package com.freisia.cataloguemoviedb

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.freisia.cataloguemoviedb.di.component.*
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException

open class App : Application(), ComponentProvider {

    private lateinit var instance: Application

    private val coreComponent: CoreComponent by lazy {
        initializeCoreComponent()
    }

    override fun provideListComponent(): ListMovieComponent {
        return initializedListComponent()
    }

    override fun provideDetailComponent(): DetailComponent {
        return initializeDetailComponent()
    }

    override fun provideCoreComponent(): CoreComponent {
        return initializeCoreComponent()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        try{
            YoutubeDL.getInstance().init(this)
        } catch (e: YoutubeDLException){
            Log.e("1432", "failed to initialize youtubedl-android", e)
            Toast.makeText(applicationContext, "initialization failed: " + e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeCoreComponent() = DaggerCoreComponent.factory().create(instance)
    private fun initializeDetailComponent() = DaggerDetailComponent.factory().create(coreComponent)
    private fun initializedListComponent() = DaggerListMovieComponent.factory().create(coreComponent)
}