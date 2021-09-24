package com.ibunda.mitrailifeapps.data.model

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
    var latitude: Double? = null,
    var longitude: Double? = null,
    var isNew: Boolean? = false,
    var registeredToken: String? = null,
    @get:Exclude
    var isAuthenticated: Boolean = false,
    @get:Exclude
    var isCreated: Boolean = false,
    @get:Exclude
    var errorMessage: String? = null

) : Parcelable