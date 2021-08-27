package com.ibunda.mitrailifeapps.utils

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import com.ibunda.mitrailifeapps.R
import java.util.*

class DatePickerHelper(context: Context) {
    private var dialog: DatePickerDialog
    private var callback: Callback? = null
    private val listener =
        DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            callback?.onDateSelected(datePicker ,dayOfMonth, monthOfYear, year)
        }
    init {
        val style = R.style.DialogTheme
        val cal = Calendar.getInstance()
        dialog = DatePickerDialog(context, style, listener,
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
    }
    fun showDialog(dayofMonth: Int, month: Int, year: Int, callback: Callback?) {
        this.callback = callback
        dialog.datePicker.init(year, month, dayofMonth, null)
        dialog.show()
    }
    fun setMinDate(minDate: Long) {
        dialog.datePicker.minDate = minDate
    }
    fun setMaxDate(maxDate: Long) {
        dialog.datePicker.maxDate = maxDate
    }
    interface Callback {
        fun onDateSelected(datePicker: View, dayofMonth: Int, month: Int, year: Int)
    }
}