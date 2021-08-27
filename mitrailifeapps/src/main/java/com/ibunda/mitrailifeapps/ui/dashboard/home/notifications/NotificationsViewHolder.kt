package com.ibunda.mitrailifeapps.ui.dashboard.home.notifications

import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Notifications
import com.ibunda.mitrailifeapps.databinding.ItemRvNotificationBinding
import com.ibunda.mitrailifeapps.utils.loadImage

class NotificationsViewHolder (private val binding: ItemRvNotificationBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Notifications) {
        with(binding) {

            if (!data.read!!) {
                linearNotif.setBackgroundResource(R.color.bgChatNotif)
            }
            imgProfile.loadImage(data.userPicture)
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