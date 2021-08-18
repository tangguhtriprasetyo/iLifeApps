package com.ibunda.ilifeapps.ui.dashboard.search.otherlistshop

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.databinding.ItemRvShopLainnyaBinding
import com.ibunda.ilifeapps.utils.PriceFormatHelper
import com.ibunda.ilifeapps.utils.loadImage

class OtherListShopViewHolder(private val binding: ItemRvShopLainnyaBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Shops) {
        with(binding) {

            if (data.verified == true) {
                icVerified.visibility = View.VISIBLE
            }
            imgProfile.loadImage(data.shopPicture)
            tvNamaMitra.text = (data.shopName)
            tvKategoriMitra.text = (data.categoryName)
            tvPrice.text = PriceFormatHelper.getPriceFormat(data.price)

            with(itemView) {
                setOnClickListener {

                }
            }

        }
    }


}