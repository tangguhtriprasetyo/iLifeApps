package com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.ulasan

import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Ulasan
import com.ibunda.ilifeapps.databinding.ItemRvUlasanBinding

class UlasanViewHolder(private val binding: ItemRvUlasanBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Ulasan) {
        with(binding) {

            ratingBar.rating = (data.rating?.toFloat()!!)
            tvNamaCustomer.text = (data.userName)
            tvValueUlasan.text = (data.ulasan)
            tvDate.text = (data.date)

            with(itemView) {
                setOnClickListener {

                }
            }

        }
    }

}