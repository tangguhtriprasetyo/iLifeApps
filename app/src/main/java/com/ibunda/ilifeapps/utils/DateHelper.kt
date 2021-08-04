package com.ibunda.ilifeapps.utils

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    fun getCurrentDate(): String {
        val localeID = Locale("in", "ID")
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", localeID)
        val date = Date()
        return dateFormat.format(date)
    }



}