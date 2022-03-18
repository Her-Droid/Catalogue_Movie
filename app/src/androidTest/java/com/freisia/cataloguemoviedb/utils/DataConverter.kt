package com.freisia.cataloguemoviedb.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStreamReader

object DataConverter {
    fun <T> convertClassToString(value: T) : String{
        val gson = Gson()
        return gson.toJson(value)
    }

    fun <T> convertStringToClass(value: String, javaClass: Class<T>): T{
        return Gson().fromJson(value,javaClass)
    }

    fun readStringFromFile(fileName: String): String {
        try {
            val applicationContext = ApplicationProvider.getApplicationContext<Context>()
            val inputStream = applicationContext.assets.open(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }


}