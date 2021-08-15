package com.ibunda.ilifeapps.ui.dashboard.home.story

import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Ads
import com.ibunda.ilifeapps.databinding.ItemRvStoryMitraBinding
import com.ibunda.ilifeapps.utils.loadImage

class StoryHomeViewHolder (private val binding: ItemRvStoryMitraBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Ads) {
        with(binding) {
            imgStoryMitra.loadImage(data.linkImg)
            tvJudulStoryMitra.text = data.title
            tvDeskripsiStoryMitra.text = data.body
        }
    }
}