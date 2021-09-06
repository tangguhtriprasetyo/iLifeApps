package com.ibunda.mitrailifeapps.ui.dashboard.order

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.databinding.ItemRvPesananBinding
import com.ibunda.mitrailifeapps.ui.detailorder.DetailActivity
import com.ibunda.mitrailifeapps.utils.loadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class OrderViewHolder (private val binding: ItemRvPesananBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Orders) {
        with(binding) {

            imgProfileMitra.loadImage(data.userPicture)
            tvNamaMitraPesanan.text = (data.userName)
            tvAddress.text = (data.address)
            tvDate.text = (data.createdAt)

            with(itemView) {
                setOnClickListener {
                    val intent =
                        Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_ORDER, data)
                    context.startActivity(intent)
                }
            }

        }
    }


}