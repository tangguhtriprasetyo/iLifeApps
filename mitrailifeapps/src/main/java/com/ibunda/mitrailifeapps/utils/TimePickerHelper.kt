package com.ibunda.mitrailifeapps.utils

import android.app.TimePickerDialog
import android.content.Context
import com.ibunda.mitrailifeapps.R
import java.util.*

class TimePickerHelper(
    context: Context,
    is24HourView: Boolean
) {
    private var dialog: TimePickerDialog
    private var callback: Callback? = null
    private val listener = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
        callback?.onTimeSelected(hourOfDay, minute)
    }
    init {
        val style = R.style.DialogTheme
        val cal = Calendar.getInstance()
        dialog = TimePickerDialog(context, style, listener,
            cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), is24HourView)
    }
    fun showDialog(hourOfDay: Int, minute: Int, callback: Callback?) {
        this.callback = callback
        dialog.updateTime(hourOfDay, minute)
        dialog.show()
    }
    interface Callback {
        fun onTimeSelected(hourOfDay: Int, minute: Int)
    }
}