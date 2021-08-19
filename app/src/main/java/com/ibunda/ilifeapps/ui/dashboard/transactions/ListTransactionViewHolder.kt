package com.ibunda.ilifeapps.ui.dashboard.transactions

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.databinding.ItemRvContentTransactionBinding
import com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.DetailActivity
import com.ibunda.ilifeapps.utils.loadImage

class ListTransactionViewHolder(private val binding: ItemRvContentTransactionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Orders) {
        with(binding) {

            if (data.verified == true) {
                icVerified.visibility = View.VISIBLE
            }

            if (data.orderKhusus == true) {
                imgProfileMitra.loadImage(data.userPicture)
                tvNamaMitraPesanan.text = data.userName
            } else {
                imgProfileMitra.loadImage(data.shopPicture)
                tvNamaMitraPesanan.text = data.shopName
            }

            tvKategoriPesanan.text = data.categoryName
            tvDate.text = data.orderDate
            tvHarga.text = data.totalPrice
            tvAddress.text = data.address

            with(itemView) {
                setOnClickListener {
                    val intent =
                        Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_ORDER, data)
                    context.startActivity(intent)
                }
            }

        }
    }
}