package com.ibunda.ilifeapps.ui.dashboard.home.notifications

import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Notifications
import com.ibunda.ilifeapps.databinding.ItemRvNotificationBinding
import com.ibunda.ilifeapps.utils.loadImage

class NotificationsViewHolder(
    private val binding: ItemRvNotificationBinding,
    private val notificationsClickCallback: NotificationsClickCallback
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Notifications) {
        with(binding) {

            imgProfile.loadImage(data.senderPicture)
            tvTitle.text = (data.title)
            tvBody.text = (data.body)
            tvDate.text = (data.date)

            with(itemView) {
                setOnClickListener {
                    notificationsClickCallback.onItemClicked(data)
                }
            }

        }
    }
}