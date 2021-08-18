package com.ibunda.ilifeapps.ui.dashboard.transactions

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.databinding.ItemRvContentTransactionBinding
import com.ibunda.ilifeapps.utils.loadImage

class ListTransactionViewHolder(private val binding: ItemRvContentTransactionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Orders) {
        with(binding) {

            if (data.verified == true) {
                icVerified.visibility = View.VISIBLE
            }
            imgProfileMitra.loadImage(data.shopPicture)
            tvNamaMitraPesanan.text = data.shopName
            tvKategoriPesanan.text = data.categoryName
            tvDate.text = data.orderDate
            tvHarga.text = data.totalPrice
            tvAddress.text = data.address

            with(itemView) {
//                TODO("GOTO DETAIL TRANSACTION")
            }

        }
    }
}