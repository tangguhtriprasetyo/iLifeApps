package com.ibunda.ilifeapps.ui.dashboard.search.promolistshop

import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.databinding.ItemRvSedangDiskonBinding
import com.ibunda.ilifeapps.utils.PriceFormatHelper
import com.ibunda.ilifeapps.utils.loadImage

class PromoListShopViewHolder(private val binding: ItemRvSedangDiskonBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Shops) {
        with(binding) {

            if (data.verified == true) {
                icVerified.visibility = View.VISIBLE
            }
            imgProfile.loadImage(data.shopPicture)
            tvNamaMitra.text = (data.shopName)
            tvKategoriMitra.text = (data.categoryName)

            tvHargaSebelumDiskon.text = PriceFormatHelper.getPriceFormat(data.price)
            tvHargaSebelumDiskon.paintFlags =
                tvHargaSebelumDiskon.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            val totalPrice = Math.abs(data.price!!) - Math.abs(data.shopPromo!!)
            tvHargaSesudahDiskon.text = PriceFormatHelper.getPriceFormat(totalPrice)

            with(itemView) {
                setOnClickListener {
//
                }
            }

        }
    }


}