package com.ibunda.mitrailifeapps.ui.dashboard.profile.ulasan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.data.model.Ulasan
import com.ibunda.mitrailifeapps.databinding.ItemRvUlasanBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UlasanAdapter : RecyclerView.Adapter<UlasanViewHolder>() {
    private var listUlasan = ArrayList<Ulasan>()

    fun setListUlasan(ulasan: List<Ulasan>?) {
        if (ulasan == null) return
        this.listUlasan.clear()
        this.listUlasan.addAll(ulasan)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UlasanViewHolder {
        val itemRvUlasanBinding =
            ItemRvUlasanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return UlasanViewHolder(itemRvUlasanBinding)
    }

    override fun onBindViewHolder(holder: UlasanViewHolder, position: Int) {
        val ulasan = listUlasan[position]
        holder.bind(ulasan)
    }

    override fun getItemCount(): Int = listUlasan.size

}