package com.ibunda.ilifeapps.ui.dashboard.search.promolistshop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.databinding.ItemRvSedangDiskonBinding

class PromoListShopAdapter : RecyclerView.Adapter<PromoListShopViewHolder>() {

    private var listShops = ArrayList<Shops>()

    fun setListShops(shops: List<Shops>?) {
        if (shops == null) return
        this.listShops.clear()
        this.listShops.addAll(shops)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoListShopViewHolder {
        val itemRvSedangDiskonBinding =
            ItemRvSedangDiskonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PromoListShopViewHolder(itemRvSedangDiskonBinding)
    }

    override fun onBindViewHolder(holder: PromoListShopViewHolder, position: Int) {
        val shops = listShops[position]
        holder.bind(shops)
    }

    override fun getItemCount(): Int = listShops.size


}