package com.ibunda.mitrailifeapps.data.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shops(

    var address: String? = null,
    var categoryName: String? = null,
    var facebook: String? = null,
    var instagram: String? = null,
    var promo: Boolean? = null,
    var verified: Boolean? = false,
    var kemampuan1: String? = null,
    var kemampuan2: String? = null,
    var kemampuan3: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var mitraId: String? = null,
    var price: Int? = null,
    var rating: Double? = null,
    var registeredAt: String? = null,
    var shopId: String? = null,
    var shopName: String? = null,
    var shopPicture: String? = null,
    var shopPromo: Int? = null,
    var totalPesananSukses: Int? = null,
    var totalUlasan: Int? = null,
    @get:Exclude
    var isAuthenticated: Boolean? = null,
    @get:Exclude
    var isCreated: Boolean? = null,
    @get:Exclude
    var errorMessage: String? = null

) : Parcelable
