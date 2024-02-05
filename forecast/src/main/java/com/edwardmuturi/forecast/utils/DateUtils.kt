package com.edwardmuturi.forecast.utils

import java.text.SimpleDateFormat
import java.util.Calendar

object DateUtils {
    fun getNextFiveDays(): List<String> {
        val dateFormat = SimpleDateFormat("EEEE")
        val calendar = Calendar.getInstance()

        val nextFiveDays = mutableListOf<String>()

        repeat(5) {
            calendar.add(Calendar.DAY_OF_WEEK, 1)
            nextFiveDays.add(dateFormat.format(calendar.time))
        }

        return nextFiveDays
    }
}
