package com.example.ilifeapps.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(

    var username: String? = null
) : Parcelable
