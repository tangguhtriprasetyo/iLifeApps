package com.ibunda.ilifeapps.ui.dashboard.home.ads

import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Ads
import com.ibunda.ilifeapps.databinding.ItemRvAdsHomeBinding
import com.ibunda.ilifeapps.utils.loadImage

class AdsHomeViewHolder(private val binding: ItemRvAdsHomeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Ads) {
        with(binding) {
            imgAdsHome.loadImage(data.linkImg)
        }
    }
}