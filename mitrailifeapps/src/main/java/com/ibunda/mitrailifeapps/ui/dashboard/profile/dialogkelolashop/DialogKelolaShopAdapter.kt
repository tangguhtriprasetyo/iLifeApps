package com.ibunda.mitrailifeapps.ui.dashboard.profile.dialogkelolashop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.ItemRvDialogKelolaTokoBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DialogKelolaShopAdapter (private val mDialogClickCallback : DialogKelolaShopClickCallback) : RecyclerView.Adapter<DialogKelolaShopViewHolder>() {
    private var listShops = ArrayList<Shops>()

    fun setListShops(shops: List<Shops>?) {
        if (shops == null) return
        this.listShops.clear()
        this.listShops.addAll(shops)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogKelolaShopViewHolder {
        val itemRvDialogKelolaTokoBinding =
            ItemRvDialogKelolaTokoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return DialogKelolaShopViewHolder(mDialogClickCallback, itemRvDialogKelolaTokoBinding)
    }

    override fun onBindViewHolder(holder: DialogKelolaShopViewHolder, position: Int) {
        val shops = listShops[position]
        holder.bind(shops)
    }

    override fun getItemCount(): Int = listShops.size

}