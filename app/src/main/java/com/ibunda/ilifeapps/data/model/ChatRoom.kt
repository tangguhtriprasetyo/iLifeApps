package com.ibunda.ilifeapps.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatRoom(

    var chatRoomId: String? = null,
    var accTawar: Boolean = false,
    var lastDate: String? = null,
    var lastMessage: String? = null,
    var lastHargaTawar: String? = null,
    var lastTawar: Boolean = false,
    var read: Boolean = false,
    var shopId: String? = null,
    var shopName: String? = null,
    var shopPicture: String? = null,
    var shopPrice: String? = null,
    var categoryName: String? = null,
    var userId: String? = null,
    var userName: String? = null,
    var userPicture: String? = null,
    var verified: Boolean = false

) : Parcelable