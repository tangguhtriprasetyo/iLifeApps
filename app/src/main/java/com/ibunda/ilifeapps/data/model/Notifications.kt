package com.ibunda.ilifeapps.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notifications(

    var notifId: String? = null,
    var body: String? = null,
    var date: String? = null,
    var orderId: String? = null,
    var receiverId: String? = null,
    var receiverPicture: String? = null,
    var title: String? = null,
    var senderId: String? = null,
    var senderPicture: String? = null

) : Parcelable
