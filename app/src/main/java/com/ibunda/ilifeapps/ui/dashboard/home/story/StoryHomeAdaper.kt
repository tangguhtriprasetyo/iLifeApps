package com.ibunda.ilifeapps.ui.dashboard.home.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Ads
import com.ibunda.ilifeapps.databinding.ItemRvStoryMitraBinding

class StoryHomeAdaper : RecyclerView.Adapter<StoryHomeViewHolder>() {

    private var listStory = ArrayList<Ads>()

    fun setListStory(ads: List<Ads>?) {
        if (ads == null) return
        this.listStory.clear()
        this.listStory.addAll(ads)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryHomeViewHolder {
        val itemRvStoryMitraBinding =
            ItemRvStoryMitraBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryHomeViewHolder(itemRvStoryMitraBinding)
    }

    override fun onBindViewHolder(holder: StoryHomeViewHolder, position: Int) {
        val shops = listStory[position]
        holder.bind(shops)
    }

    override fun getItemCount(): Int = listStory.size


}