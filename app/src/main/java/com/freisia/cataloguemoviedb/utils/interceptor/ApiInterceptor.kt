package com.freisia.cataloguemoviedb.utils.interceptor

import com.freisia.cataloguemoviedb.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequestUrl =
            chain.request()
                .url
                .newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()

        return chain.proceed(
            chain.request()
                .newBuilder()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .url(newRequestUrl)
                .build()
        )
    }
}