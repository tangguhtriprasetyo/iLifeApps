package com.example.ilifeapps.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(

    var address: String? = null,
    var avatar: String? = null,
    var email: String? = null,
    var gender: String? = null,
    var name: String? = null,
    var phone: String? = null,
    var tanggalDibuat: String? = null,
    var ttl: String? = null,
    var totalOrder: Int? = null,
    var latitude: Int? = null,
    var longitude: Int? = null

) : Parcelable
