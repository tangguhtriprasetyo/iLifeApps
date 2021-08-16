package com.ibunda.ilifeapps.ui.listmitra.listshop

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.databinding.ItemRvListShopBinding
import com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.DetailShopFragment
import com.ibunda.ilifeapps.utils.PriceFormatHelper
import com.ibunda.ilifeapps.utils.loadImage


class ListShopViewHolder (private val binding: ItemRvListShopBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Shops) {
        with(binding) {

            if (data.verified == true) {
                icVerified.visibility = View.VISIBLE
            }
            Log.d(data.verified.toString(), "verified")
            imgProfileShops.loadImage(data.shopPicture)
            ratingBar.rating = (data.rating?.toFloat()!!)
            tvNamaMitra.text = (data.shopName)

            tvHargaMitra.text = PriceFormatHelper.getPriceFormat(data.price)

            with(itemView) {
                setOnClickListener {
                    val mDetailShopFragment = DetailShopFragment()
                    val mBundle = Bundle()
                    mBundle.putParcelable(DetailShopFragment.EXTRA_SHOP_DATA, data)
                    mDetailShopFragment.arguments = mBundle
                    val manager: FragmentManager =
                        (context as AppCompatActivity).supportFragmentManager
                    manager.commit {
                        addToBackStack(null)
                        replace(com.ibunda.ilifeapps.R.id.host_listshop_activity, mDetailShopFragment)
                    }
                }
            }

        }
    }


}