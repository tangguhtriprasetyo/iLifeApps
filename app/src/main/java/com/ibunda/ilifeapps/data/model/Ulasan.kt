package com.ibunda.ilifeapps.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ulasan(

    var date: String? = null,
    var rating: Double? = null,
    var shopId: String? = null,
    var shopName: String? = null,
    var shopPicture: String? = null,
    var ulasan: String? = null,
    var userId: String? = null,
    var userName: String? = null,
    var userPicture: String? = null,
    var verified: Boolean = false

) : Parcelable