package com.ibunda.ilifeapps.ui.dashboard.home.ads

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Ads
import com.ibunda.ilifeapps.databinding.ItemRvAdsHomeBinding

class AdsHomeAdapter : RecyclerView.Adapter<AdsHomeViewHolder>() {
    private var listAds = ArrayList<Ads>()

    fun setListAds(ads: List<Ads>?) {
        if (ads == null) return
        this.listAds.clear()
        this.listAds.addAll(ads)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsHomeViewHolder {
        val itemRvAdsHomeBinding =
            ItemRvAdsHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdsHomeViewHolder(itemRvAdsHomeBinding)
    }

    override fun onBindViewHolder(holder: AdsHomeViewHolder, position: Int) {
        val shops = listAds[position]
        holder.bind(shops)
    }

    override fun getItemCount(): Int = listAds.size


}