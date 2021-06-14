package com.ibunda.ilifeapps.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ads(

    var category: String? = null,
    var linkImg: String? = null,
    var body: String? = null,
    var title: String? = null,
    var url: String? = null


) : Parcelable