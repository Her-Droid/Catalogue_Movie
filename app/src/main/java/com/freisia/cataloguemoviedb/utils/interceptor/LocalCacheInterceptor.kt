package com.freisia.cataloguemoviedb.utils.interceptor

import android.content.Context
import com.idlavonuyab.sp2hpperusahaan.utils.NetworkUtils
import okhttp3.Interceptor
import okhttp3.Response

class LocalCacheInterceptor(private val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = if (NetworkUtils.hasNetwork(context)) {
            request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
        } else {
            request.newBuilder().header(
                "Cache-Control",
                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
            ).build()
        }
        return chain.proceed(request)
    }
}