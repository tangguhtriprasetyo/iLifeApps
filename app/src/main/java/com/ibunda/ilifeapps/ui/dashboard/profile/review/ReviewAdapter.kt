package com.ibunda.ilifeapps.ui.dashboard.profile.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Ulasan
import com.ibunda.ilifeapps.databinding.ItemRvPenilaianBinding

class ReviewAdapter : RecyclerView.Adapter<ReviewViewHolder>() {

    private var listUlasan = ArrayList<Ulasan>()

    fun setListUlasan(ulasan: List<Ulasan>?) {
        if (ulasan == null) return
        this.listUlasan.clear()
        this.listUlasan.addAll(ulasan)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val itemRvPenilaianBinding =
            ItemRvPenilaianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(itemRvPenilaianBinding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val shops = listUlasan[position]
        holder.bind(shops)
    }

    override fun getItemCount(): Int = listUlasan.size

}