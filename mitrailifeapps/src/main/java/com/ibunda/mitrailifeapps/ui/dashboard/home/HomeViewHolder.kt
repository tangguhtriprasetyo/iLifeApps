package com.ibunda.mitrailifeapps.ui.dashboard.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.databinding.ItemRvHomeBinding
import com.ibunda.mitrailifeapps.ui.dashboard.home.detailpesanankhusus.DetailPesananKhususFragment
import com.ibunda.mitrailifeapps.utils.loadImage

class HomeViewHolder (private val binding: ItemRvHomeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Orders) {
        with(binding) {

            imgProfileMitra.loadImage(data.userPicture)
            tvNamaMitraPesanan.text = (data.userName)
            tvKategoriPesanan.text = (data.categoryName)
            tvAddress.text = (data.address)
            tvDate.text = (data.createdAt)
            tvHarga.text = (data.totalPrice)

            with(itemView) {
                setOnClickListener {
                    val mDetailPesananKhususFragment = DetailPesananKhususFragment()
                    val mBundle = Bundle()
                    mBundle.putParcelable(DetailPesananKhususFragment.EXTRA_ORDER_DATA, data)
                    mDetailPesananKhususFragment.arguments = mBundle
                    val manager: FragmentManager =
                        (context as AppCompatActivity).supportFragmentManager
                    manager.commit {
                        addToBackStack(null)
                        replace(com.ibunda.mitrailifeapps.R.id.host_fragment_activity_main, mDetailPesananKhususFragment)
                    }
                }
            }

        }
    }


}