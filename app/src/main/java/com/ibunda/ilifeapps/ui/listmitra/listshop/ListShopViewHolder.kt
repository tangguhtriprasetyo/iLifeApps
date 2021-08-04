package com.ibunda.ilifeapps.ui.listmitra.listshop

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.databinding.ItemRvListShopBinding
import com.ibunda.ilifeapps.utils.loadImage
import java.text.NumberFormat
import java.util.*

class ListShopViewHolder (private val binding: ItemRvListShopBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Shops) {
        with(binding) {

            if (data.verified == true) {
                icVerified.visibility = View.VISIBLE
            }
            imgProfileShops.loadImage(data.shopPicture)
            ratingBar.rating = (data.rating?.toFloat()!!)
            tvNamaMitra.text = (data.shopName)


            val localeId = Locale("in", "ID")
            val priceFormat = NumberFormat.getCurrencyInstance(localeId)
            tvHargaMitra.text = priceFormat.format(data.price)

            with(itemView) {
                setOnClickListener {
                    //
                }
            }

        }
    }
}