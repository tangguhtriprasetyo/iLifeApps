package com.ibunda.ilifeapps.ui.dashboard.search.otherlistshop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.databinding.ItemRvShopLainnyaBinding

class OtherListShopAdapter : RecyclerView.Adapter<OtherListShopViewHolder>() {

    private var listShops = ArrayList<Shops>()

    fun setListShops(shops: List<Shops>?) {
        if (shops == null) return
        this.listShops.clear()
        this.listShops.addAll(shops)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherListShopViewHolder {
        val itemRvShopLainnyaBinding =
            ItemRvShopLainnyaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OtherListShopViewHolder(itemRvShopLainnyaBinding)
    }

    override fun onBindViewHolder(holder: OtherListShopViewHolder, position: Int) {
        val shops = listShops[position]
        holder.bind(shops)
    }

    override fun getItemCount(): Int = listShops.size


}