package com.freisia.cataloguemoviedb.di.module

import android.content.Context
import com.freisia.cataloguemoviedb.data.source.network.ApiService
import com.freisia.cataloguemoviedb.utils.interceptor.ApiInterceptor
import com.freisia.cataloguemoviedb.utils.interceptor.CacheInterceptor
import com.freisia.cataloguemoviedb.utils.interceptor.LocalCacheInterceptor
import com.freisia.cataloguemoviedb.utils.interceptor.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [TestEndpointModule::class])
class TestNetworkModule {
    @Singleton
    @Provides
    fun providesNetwork(context: Context, url: String): Retrofit {
        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)
        val okHttpClient = OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor(NetworkConnectionInterceptor(context))
            .addInterceptor(LocalCacheInterceptor(context))
            .addNetworkInterceptor(ApiInterceptor())
            .addNetworkInterceptor(CacheInterceptor())
            .retryOnConnectionFailure(false)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

}
