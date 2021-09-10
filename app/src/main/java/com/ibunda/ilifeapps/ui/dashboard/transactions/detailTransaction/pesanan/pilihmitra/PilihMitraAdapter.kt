package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.pilihmitra

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.databinding.ItemRvPilihMitraBinding

class PilihMitraAdapter(private val mPilihMitraClickCallback: PilihMitraClickCallback) : RecyclerView.Adapter<PilihMitraViewHolder>() {
    private var listMitra = ArrayList<Shops>()

    fun setListMitra(shops: List<Shops>?) {
        if (shops == null) return
        this.listMitra.clear()
        this.listMitra.addAll(shops)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PilihMitraViewHolder {
        val itemRvPilihMitraBinding =
            ItemRvPilihMitraBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return PilihMitraViewHolder(itemRvPilihMitraBinding, mPilihMitraClickCallback)
    }

    override fun onBindViewHolder(holder: PilihMitraViewHolder, position: Int) {
        val shops = listMitra[position]
        holder.bind(shops)
    }

    override fun getItemCount(): Int = listMitra.size
}