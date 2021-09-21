package com.ibunda.ilifeapps.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatMessages(
    var date: String? = null,
    var message: String? = null,
    var sender: String? = null,
    var statusTawaran: String? = null,
    var tawar: Boolean = false,
    var time: String? = null,
    @ServerTimestamp
    val timeStamp: Timestamp? = null
) : Parcelable