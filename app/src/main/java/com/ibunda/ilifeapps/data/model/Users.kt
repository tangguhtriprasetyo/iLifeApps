package com.ibunda.ilifeapps.data.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(

    var userId: String? = null,
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
    var longitude: Int? = null,
    @get:Exclude
    var isAuthenticated: Boolean? = null,
    @get:Exclude
    var isNew: Boolean? = null,
    @get:Exclude
    var isCreated: Boolean? = null

) : Parcelable
