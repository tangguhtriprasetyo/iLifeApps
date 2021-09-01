package com.ibunda.mitrailifeapps.ui.dashboard.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.databinding.ItemRvPesananBinding

class OrderAdapter : RecyclerView.Adapter<OrderViewHolder>() {
    private var listOrders = ArrayList<Orders>()

    fun setListOrders(orders: List<Orders>?) {
        if (orders == null) return
        this.listOrders.clear()
        this.listOrders.addAll(orders)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemRvPesananBinding =
            ItemRvPesananBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return OrderViewHolder(itemRvPesananBinding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orders = listOrders[position]
        holder.bind(orders)
    }

    override fun getItemCount(): Int = listOrders.size

}