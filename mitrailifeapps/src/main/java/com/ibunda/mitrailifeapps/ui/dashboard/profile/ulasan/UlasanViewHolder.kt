package com.ibunda.mitrailifeapps.ui.dashboard.profile.ulasan

import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.data.model.Ulasan
import com.ibunda.mitrailifeapps.databinding.ItemRvUlasanBinding
import com.ibunda.mitrailifeapps.utils.loadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UlasanViewHolder (private val binding: ItemRvUlasanBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Ulasan) {
        with(binding) {

            imgProfile.loadImage(data.userPicture)
            tvNamaUser.text = (data.userName)
            tvValueUlasan.text = (data.ulasan)
            ratingBar.rating = (data.rating?.toFloat()!!)
            tvDate.text = (data.date)

            with(itemView) {
                setOnClickListener {
                }
            }

        }
    }


}