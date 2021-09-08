package com.ibunda.mitrailifeapps.ui.dashboard.profile.dialogkelolashop

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.ItemRvDialogKelolaTokoBinding
import com.ibunda.mitrailifeapps.utils.loadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DialogKelolaShopViewHolder (private val mDialogClickCallback : DialogKelolaShopClickCallback, private val binding: ItemRvDialogKelolaTokoBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Shops) {
        val preferences = itemView.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val activeShop = preferences.getString("shopId", "none")
        with(binding) {

            if (activeShop == data.shopId) {
                rbToko.isChecked = true
            }
            imgTokoMitra.loadImage(data.shopPicture)
            tvNamaToko.text = (data.shopName)

            with(itemView) {
                setOnClickListener {
                    mDialogClickCallback.onItemClicked(data)
                    rbToko.isChecked = true
                }
            }

        }
    }

    companion object {
        const val PREFS_NAME = "mitra_pref"
    }

}