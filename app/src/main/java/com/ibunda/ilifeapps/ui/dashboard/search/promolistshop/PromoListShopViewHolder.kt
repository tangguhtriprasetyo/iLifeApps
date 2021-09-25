package com.ibunda.ilifeapps.ui.dashboard.search.promolistshop

import android.content.Intent
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.ItemRvSedangDiskonBinding
import com.ibunda.ilifeapps.ui.listmitra.ListMitraActivity
import com.ibunda.ilifeapps.utils.PriceFormatHelper
import com.ibunda.ilifeapps.utils.loadImage

class PromoListShopViewHolder(private val binding: ItemRvSedangDiskonBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Shops, userData: Users) {
        with(binding) {

            if (data.verified) {
                icVerified.visibility = View.VISIBLE
            }
            imgProfile.loadImage(data.shopPicture)
            tvNamaMitra.text = (data.shopName)
            tvKategoriMitra.text = (data.categoryName)

            tvHargaSebelumDiskon.text = PriceFormatHelper.getPriceFormat(data.price)
            tvHargaSebelumDiskon.paintFlags =
                tvHargaSebelumDiskon.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            tvHargaSesudahDiskon.text = PriceFormatHelper.getPriceFormat(data.shopPromo)

            with(itemView) {
                setOnClickListener {
                    val intent =
                        Intent(context, ListMitraActivity::class.java)
                    intent.putExtra(ListMitraActivity.EXTRA_SHOP, data.shopId)
                    intent.putExtra(ListMitraActivity.EXTRA_USER, userData.userId)
                    context.startActivity(intent)
                }
            }

        }
    }


}