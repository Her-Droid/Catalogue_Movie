package com.freisia.cataloguemoviedb.utils.exception

import android.content.Context
import com.freisia.cataloguemoviedb.R
import java.io.IOException

class NoConnectivityException(private val context: Context): IOException(){
    override val message: String
        get() = context.getString(R.string.no_internet_connection)
}