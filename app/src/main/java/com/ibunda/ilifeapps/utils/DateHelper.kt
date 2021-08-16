package com.ibunda.ilifeapps.utils

import java.text.SimpleDateFormat
import java.util.*




object DateHelper {

    fun getCurrentDateTime(): String {
        val localeID = Locale("in", "ID")
        val dateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", localeID)
        val date = Date()
        return dateFormat.format(date)
    }

    fun getCurrentDate(): String {
        val localeID = Locale("in", "ID")
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", localeID)
        val date = Date()
        return dateFormat.format(date)
    }
}