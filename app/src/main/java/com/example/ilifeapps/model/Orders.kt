package com.example.ilifeapps.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Orders(

    var address: String? = null,
    var arrivedAt: String? = null,
    var canceledAt: String? = null,
    var canceledBy: String? = null,
    var canceledReason: String? = null,
    var categoryName: String? = null,
    var createdAt: String? = null,
    var finishedAt: String? = null,
    var isFood: Boolean? = null,
    var isKhusus: Boolean? = null,
    var isLainnya: Boolean? = null,
    var latitude: Int? = null,
    var longitude: Int? = null,
    var orderDate: String? = null,
    var orderTime: String? = null,
    var payment: String? = null,
    var processedAt: String? = null,
    var shopId: String? = null,
    var shopName: String? = null,
    var shopPicture: String? = null,
    var startAt: String? = null,
    var status: String? = null,
    var totalPrice: String? = null,
    var userId: String? = null,
    var userName: String? = null,
    var userPicture: String? = null,
    var verified: Boolean? = null

) : Parcelable
