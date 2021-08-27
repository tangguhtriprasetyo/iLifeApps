package com.ibunda.mitrailifeapps.utils

import java.text.NumberFormat
import java.util.*

object PriceFormatHelper {

    fun getPriceFormat(format: Int?): String? {
        val localeId = Locale("in", "ID")
        val priceFormat = NumberFormat.getCurrencyInstance(localeId)
        return priceFormat.format(format)
    }

}