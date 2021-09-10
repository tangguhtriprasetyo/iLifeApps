package com.ibunda.mitrailifeapps.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OfferOrder(

    var orderId: String? = null,

    var tawarId: String? = null,
    var shopId: String? = null,
    var shopName: String? = null,
    var shopPicture: String? = null,
    var userId: String? = null,
    var verified: Boolean? = null,
    var priceTawar: String? = null,
    var rating: Double? = null

) : Parcelable