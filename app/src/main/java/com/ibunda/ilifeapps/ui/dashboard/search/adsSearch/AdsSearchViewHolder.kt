package com.ibunda.ilifeapps.ui.dashboard.search.adsSearch

import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Ads
import com.ibunda.ilifeapps.databinding.ItemRvAdsSearchBinding
import com.ibunda.ilifeapps.utils.loadImage

class AdsSearchViewHolder(private val binding: ItemRvAdsSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Ads) {
        with(binding) {
            itemRvImgAds.loadImage(data.linkImg)
        }
    }
}