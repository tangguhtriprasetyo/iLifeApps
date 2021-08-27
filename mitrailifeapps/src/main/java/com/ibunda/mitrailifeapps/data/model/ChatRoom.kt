package com.ibunda.mitrailifeapps.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatRoom(

    var accTawar: Boolean? = null,
    var imgMitra: String? = null,
    var imgUser: String? = null,
    var lastDate: String? = null,
    var lastMessage: String? = null,
    var lastHargaTawar: String? = null,
    var lastTawar: Boolean? = null,
    var read: Boolean? = null,
    var shopId: String? = null,
    var shopName: String? = null,
    var userId: String? = null,
    var userName: String? = null

) : Parcelable