package com.ibunda.ilifeapps.ui.listmitra.listshop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.databinding.ItemRvListShopBinding

class ListShopAdapter : PagedListAdapter<Shops, ListShopViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Shops>() {
            override fun areItemsTheSame(oldItem: Shops, newItem: Shops): Boolean {
                return oldItem.shopId == newItem.shopId
            }

            override fun areContentsTheSame(oldItem: Shops, newItem: Shops): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListShopViewHolder {
        val itemRvListShopBinding =
            ItemRvListShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListShopViewHolder(itemRvListShopBinding)
    }

    override fun onBindViewHolder(holder: ListShopViewHolder, position: Int) {
        val listShop = getItem(position)
        listShop?.let { holder.bind(it) }
    }
}