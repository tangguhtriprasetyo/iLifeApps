package com.ibunda.mitrailifeapps.ui.dashboard.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.databinding.ItemRvHomeBinding

class HomeAdapter: RecyclerView.Adapter<HomeViewHolder>() {

    private var listOrders = ArrayList<Orders>()

    fun setListOrders(orders: List<Orders>?) {
        if (orders == null) return
        this.listOrders.clear()
        this.listOrders.addAll(orders)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemRvHomeBinding =
            ItemRvHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(itemRvHomeBinding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val orders = listOrders[position]
        holder.bind(orders)
    }

    override fun getItemCount(): Int = listOrders.size


}