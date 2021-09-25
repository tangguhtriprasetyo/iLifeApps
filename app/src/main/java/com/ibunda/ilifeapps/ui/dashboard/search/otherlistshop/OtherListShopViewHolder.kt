package com.ibunda.ilifeapps.ui.dashboard.search.otherlistshop

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.ItemRvShopLainnyaBinding
import com.ibunda.ilifeapps.ui.listmitra.ListMitraActivity
import com.ibunda.ilifeapps.utils.PriceFormatHelper
import com.ibunda.ilifeapps.utils.loadImage

class OtherListShopViewHolder(private val binding: ItemRvShopLainnyaBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Shops, userData: Users) {
        with(binding) {

            if (data.verified) {
                icVerified.visibility = View.VISIBLE
            }
            imgProfile.loadImage(data.shopPicture)
            tvNamaMitra.text = (data.shopName)
            tvKategoriMitra.text = (data.categoryName)
            tvPrice.text = PriceFormatHelper.getPriceFormat(data.price)

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