package com.ibunda.ilifeapps.ui.listmitra.listshop

import android.graphics.Paint
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
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class ListShopViewHolder(private val binding: ItemRvListShopBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Shops) {
        with(binding) {

            if (data.verified) {
                icVerified.visibility = View.VISIBLE
            }
            Log.d(data.verified.toString(), "verified")
            imgProfileShops.loadImage(data.shopPicture)
            ratingBar.rating = (data.rating?.toFloat()!!)
            tvNamaMitra.text = (data.shopName)

            if (data.distance != null) {

                if (data.distance!! >= 1000) {
                    val distanceText = "${data.distance!!.div(1000)} Km"
                    tvJarakMitra.text = distanceText
                } else {
                    val distanceText = "${data.distance} Meter"
                    tvJarakMitra.text = distanceText
                }

            } else {
                tvJarakMitra.text = "Tentukan Lokasi Anda"
            }

            if (data.promo) {
                tvHargaMitra.text = PriceFormatHelper.getPriceFormat(data.shopPromo)
                tvHargaMitraSebelum.visibility = View.VISIBLE
                tvHargaMitraSebelum.text = PriceFormatHelper.getPriceFormat(data.price)
                tvHargaMitraSebelum.paintFlags =
                    tvHargaMitraSebelum.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvHargaMitra.text = PriceFormatHelper.getPriceFormat(data.price)
            }

            with(itemView) {
                setOnClickListener {
                    val mDetailShopFragment = DetailShopFragment()
                    val mBundle = Bundle()
                    mBundle.putParcelable(DetailShopFragment.EXTRA_SHOP_DATA, data)
                    mBundle.putBoolean(DetailShopFragment.FROM_TRANSACTION, false)
                    mDetailShopFragment.arguments = mBundle
                    val manager: FragmentManager =
                        (context as AppCompatActivity).supportFragmentManager
                    manager.commit {
                        addToBackStack(null)
                        replace(
                            com.ibunda.ilifeapps.R.id.host_listshop_activity,
                            mDetailShopFragment
                        )
                    }
                }
            }

        }
    }
}