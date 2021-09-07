package com.ibunda.ilifeapps.ui.dashboard.search.otherlistshop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.ItemRvShopLainnyaBinding

class OtherListShopAdapter : RecyclerView.Adapter<OtherListShopViewHolder>() {

    private var listShops = ArrayList<Shops>()
    private var userData = Users()

    fun setListShops(shops: List<Shops>?, userDataProfile: Users) {
        if (shops == null) return
        this.listShops.clear()
        this.listShops.addAll(shops)
        this.userData = userDataProfile
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherListShopViewHolder {
        val itemRvShopLainnyaBinding =
            ItemRvShopLainnyaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OtherListShopViewHolder(itemRvShopLainnyaBinding)
    }

    override fun onBindViewHolder(holder: OtherListShopViewHolder, position: Int) {
        val shops = listShops[position]
        holder.bind(shops, userData)
    }

    override fun getItemCount(): Int = listShops.size


}