package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.pilihmitra

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.databinding.ItemRvPilihMitraBinding
import com.ibunda.ilifeapps.utils.loadImage

class PilihMitraViewHolder(private val binding: ItemRvPilihMitraBinding, private val mPilihMitraClickCallback: PilihMitraClickCallback) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Shops) {
        with(binding) {

            imgProfile.loadImage(data.shopPicture)
            if (data.verified) {
                icVerified.visibility = View.VISIBLE
            }
            ratingBar.rating = (data.rating?.toFloat()!!)
            tvHargaTawar.text = (data.priceTawar)
            tvNamaMitra.text = (data.shopName)

        }

        with(itemView) {
            setOnClickListener {
                mPilihMitraClickCallback.onItemClicked(data)
            }
        }
    }
}