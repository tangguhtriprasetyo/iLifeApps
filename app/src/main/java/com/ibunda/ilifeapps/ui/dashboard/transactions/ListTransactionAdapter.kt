package com.ibunda.ilifeapps.ui.dashboard.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.databinding.ItemRvContentTransactionBinding

class ListTransactionAdapter : RecyclerView.Adapter<ListTransactionViewHolder>() {
    private var listOrders = ArrayList<Orders>()


    fun setListOrders(orders: List<Orders>?) {
        if (orders == null) return
        this.listOrders.clear()
        this.listOrders.addAll(orders)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTransactionViewHolder {
        val itemRvContentTransactionBinding =
            ItemRvContentTransactionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ListTransactionViewHolder(itemRvContentTransactionBinding)
    }

    override fun onBindViewHolder(holder: ListTransactionViewHolder, position: Int) {
        val orders = listOrders[position]
        holder.bind(orders)
    }

    override fun getItemCount(): Int = listOrders.size

}