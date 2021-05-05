package com.example.ilifeapps.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shops(

    var address: String? = null,
    var categoryName: String? = null,
    var facebook: String? = null,
    var instagram: String? = null,
    var isFood: Boolean? = null,
    var isPromo: Boolean? = null,
    var verified: Boolean? = null,
    var kemampuan1: String? = null,
    var kemampuan2: String? = null,
    var kemampuan3: String? = null,
    var latitude: Int? = null,
    var longitude: Int? = null,
    var mitraId: String? = null,
    var price: Int? = null,
    var rating: Int? = null,
    var registeredAt: String? = null,
    var shopId: String? = null,
    var shopName: String? = null,
    var shopPicture: String? = null,
    var shopPromo: Int? = null,
    var totalPesananSukses: Int? = null,
    var totalUlasan: Int? = null,
) : Parcelable
