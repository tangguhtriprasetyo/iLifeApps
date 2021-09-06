package com.ibunda.ilifeapps.ui.listmitra.listshop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.ItemRvListShopBinding

class ListShopAdapter : RecyclerView.Adapter<ListShopViewHolder>() {
    private var listShops = ArrayList<Shops>()
    private var userData = Users()

    fun setListShops(shops: List<Shops>?, user: Users) {
        if (shops == null) return
        this.listShops.clear()
        this.listShops.addAll(shops)
        this.userData = user
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListShopViewHolder {
        val itemRvListShopBinding =
            ItemRvListShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListShopViewHolder(itemRvListShopBinding)
    }

    override fun onBindViewHolder(holder: ListShopViewHolder, position: Int) {
        val shops = listShops[position]
        holder.bind(shops, userData)
    }

    override fun getItemCount(): Int = listShops.size


}