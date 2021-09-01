package com.ibunda.ilifeapps.ui.dashboard.profile.review

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Ulasan
import com.ibunda.ilifeapps.databinding.ItemRvPenilaianBinding
import com.ibunda.ilifeapps.utils.loadImage

class ReviewViewHolder (private val binding: ItemRvPenilaianBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Ulasan) {
        with(binding) {

            if (data.verified == true) {
                icVerified.visibility = View.VISIBLE
            }

            imgProfile.loadImage(data.shopPicture)
            ratingBar.rating = (data.rating?.toFloat()!!)
            tvNamaMitra.text = (data.shopName)
            tvIsiPenilaian.text = (data.ulasan)
            tvDate.text = (data.date)

            with(itemView) {
                setOnClickListener {

                }
            }

        }
    }

}