package com.ibunda.ilifeapps.ui.dashboard.search.adsSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Ads
import com.ibunda.ilifeapps.databinding.ItemRvAdsSearchBinding

class AdsSearchAdapter : RecyclerView.Adapter<AdsSearchViewHolder>() {
    private var listAds = ArrayList<Ads>()

    fun setListAds(ads: List<Ads>?) {
        if (ads == null) return
        this.listAds.clear()
        this.listAds.addAll(ads)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsSearchViewHolder {
        val itemRvAdsSearchBinding =
            ItemRvAdsSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdsSearchViewHolder(itemRvAdsSearchBinding)
    }

    override fun onBindViewHolder(holder: AdsSearchViewHolder, position: Int) {
        val ads = listAds[position]
        holder.bind(ads)
    }

    override fun getItemCount(): Int = listAds.size


}