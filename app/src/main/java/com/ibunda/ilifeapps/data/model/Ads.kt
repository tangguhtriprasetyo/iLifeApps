package com.ibunda.ilifeapps.data.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ads(

    var category: String? = null,
    var linkImg: String? = null,
    var body: String? = null,
    var title: String? = null,
    var url: String? = null


) : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }
}