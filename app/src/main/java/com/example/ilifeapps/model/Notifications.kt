package com.example.ilifeapps.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notifications(

    var body: String? = null,
    var date: String? = null,
    var orderId: String? = null,
    var read: Boolean? = null,
    var receiver: String? = null,
    var shopId: String? = null,
    var shopPicture: String? = null,
    var title: String? = null,
    var userId: String? = null,
    var userPicture: String? = null

) : Parcelable
