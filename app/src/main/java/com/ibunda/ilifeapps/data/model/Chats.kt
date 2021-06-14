package com.ibunda.ilifeapps.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chats(

    var date: String? = null,
    var message: String? = null,
    var sender: String? = null,
    var statusTawaran: String? = null,
    var tawar: String? = null,
    var lastTawar: Boolean? = null,
    var time: String? = null

) : Parcelable
