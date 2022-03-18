package com.freisia.cataloguemoviedb.utils.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        val maxAge = 30
        return response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .build()
    }
}