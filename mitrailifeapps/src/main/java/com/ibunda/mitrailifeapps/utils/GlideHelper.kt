package com.ibunda.mitrailifeapps.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ibunda.mitrailifeapps.R

fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .apply(
            RequestOptions.placeholderOf(R.drawable.ic_loading)
                .error(R.drawable.ic_error))
        .into(this)
}