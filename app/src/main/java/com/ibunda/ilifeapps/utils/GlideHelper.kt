package com.ibunda.ilifeapps.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ibunda.ilifeapps.R

fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .apply(
            RequestOptions.placeholderOf(R.drawable.ic_loading)
            .error(R.drawable.ic_error))
        .into(this)
}