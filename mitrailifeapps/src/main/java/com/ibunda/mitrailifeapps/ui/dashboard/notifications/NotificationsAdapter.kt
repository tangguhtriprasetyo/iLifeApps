package com.ibunda.mitrailifeapps.ui.dashboard.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.data.model.Notifications
import com.ibunda.mitrailifeapps.databinding.ItemRvNotificationBinding

class NotificationsAdapter(private val notificationsClickCallback: NotificationsClickCallback) :
    RecyclerView.Adapter<NotificationsViewHolder>() {
    private var listNotif = ArrayList<Notifications>()

    fun setListNotif(notif: List<Notifications>?) {
        if (notif == null) return
        this.listNotif.clear()
        this.listNotif.addAll(notif)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val itemRvNotificationBinding =
            ItemRvNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return NotificationsViewHolder(itemRvNotificationBinding, notificationsClickCallback)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val notif = listNotif[position]
        holder.bind(notif)
    }

    override fun getItemCount(): Int = listNotif.size
}