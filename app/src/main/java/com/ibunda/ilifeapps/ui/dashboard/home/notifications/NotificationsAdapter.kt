package com.ibunda.ilifeapps.ui.dashboard.home.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ibunda.ilifeapps.data.model.Notifications
import com.ibunda.ilifeapps.databinding.ItemRvNotificationBinding

class NotificationsAdapter : PagedListAdapter<Notifications, NotificationsViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val itemRvNotificationBinding =
            ItemRvNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationsViewHolder(itemRvNotificationBinding)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val listNotif = getItem(position)
        listNotif?.let { holder.bind(it) }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Notifications>() {
            override fun areItemsTheSame(oldItem: Notifications, newItem: Notifications): Boolean {
                return oldItem.receiver == newItem.receiver
            }

            override fun areContentsTheSame(oldItem: Notifications, newItem: Notifications): Boolean {
                return oldItem == newItem
            }

        }
    }
}