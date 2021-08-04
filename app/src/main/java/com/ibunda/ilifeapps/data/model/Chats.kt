package com.ibunda.ilifeapps.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chats(

    //listChatRoom
    var accTawar: Boolean? = null,
    var imgMitra: String? = null,
    var imgUser: String? = null,
    var lastDate: String? = null,
    var lastHargaTawar: String? = null,
    var lastMessage: String? = null,
    var lastTawar: Boolean? = null,
    var read: Boolean? = null,
    var shopId: String? = null,
    var shopName: String? = null,
    var shopPicture: String? = null,
    var userId: String? = null,
    var userName: String? = null,
    var verified: Boolean? = null,

    //chatRoom
    var date: String? = null,
    var message: String? = null,
    var sender: String? = null,
    var statusTawaran: String? = null,
    var tawar: Boolean? = null,
    var time: String? = null

) : Parcelable
