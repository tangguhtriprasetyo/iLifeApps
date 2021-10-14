package com.ibunda.mitrailifeapps.data.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mitras(

    var mitraId: String? = null,
    var address: String? = null,
    var kecamatan: String? = null,
    var email: String? = null,
    var kodepos: String? = null,
    var kotakab: String? = null,
    var name: String? = null,
    var registeredAt: String? = null,
    var provinsi: String? = null,
    var totalShop: Int? = null,
    var verified: Boolean? = null,
    @get:Exclude
    var isAuthenticated: Boolean? = null,
    @get:Exclude
    var isCreated: Boolean? = null,
    @get:Exclude
    var errorMessage: String? = null,
    @get:Exclude
    var password: String? = null,
    @get:Exclude
    var confirmPassword: String? = null

) : Parcelable
