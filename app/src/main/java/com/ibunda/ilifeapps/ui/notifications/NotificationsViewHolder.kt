package com.ibunda.ilifeapps.ui.notifications

import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Notifications
import com.ibunda.ilifeapps.databinding.ItemRvNotificationBinding
import com.ibunda.ilifeapps.utils.loadImage

class NotificationsViewHolder (private val binding: ItemRvNotificationBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Notifications) {
        with(binding) {

            if (!data.read!!) {
                linearNotif.setBackgroundResource(R.color.bgChatNotif)
            }
            imgProfile.loadImage(data.shopPicture)
            tvTitle.text = (data.title)
            tvBody.text = (data.body)
            tvDate.text = (data.date)

            with(itemView) {
                setOnClickListener {
                    //
                }
            }

        }
    }
}