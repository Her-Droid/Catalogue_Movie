package com.freisia.cataloguemoviedb.utils

import java.text.SimpleDateFormat
import java.util.*

object DateConverter {
    fun timeAgo(date: Date) : String {
        val calendar = Calendar.getInstance()
        calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val currentCalendar = Calendar.getInstance()

        val currentYear = currentCalendar.get(Calendar.YEAR)
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentCalendar.get(Calendar.MINUTE)

        return if (year < currentYear ) {
            val interval = currentYear - year
            if (interval == 1) "last year" else "$interval years ago"
        } else if (month < currentMonth) {
            val interval = currentMonth - month
            if (interval == 1) "last month" else "$interval months ago"
        } else  if (day < currentDay) {
            val interval = currentDay - day
            if (interval == 1) "yesterday" else "$interval days ago"
        } else if (hour < currentHour) {
            val interval = currentHour - hour
            if (interval == 1) "an hour ago" else "$interval hours ago"
        } else if (minute < currentMinute) {
            val interval = currentMinute - minute
            if (interval == 1) "a minute ago" else "$interval minutes ago"
        } else {
            "a moment ago"
        }

    }

}