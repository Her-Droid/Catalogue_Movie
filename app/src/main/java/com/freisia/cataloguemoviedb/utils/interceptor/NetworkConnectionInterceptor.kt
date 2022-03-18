package com.freisia.cataloguemoviedb.utils.interceptor

import android.content.Context
import com.idlavonuyab.sp2hpperusahaan.utils.NetworkUtils
import com.freisia.cataloguemoviedb.utils.exception.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!NetworkUtils.hasNetwork(context)){
            throw NoConnectivityException(context)
        }
        val request = chain.request().newBuilder()
        return chain.proceed(request.build())
    }
}