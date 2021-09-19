package com.ibunda.ilifeapps.ui.dashboard.home.notifications

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Notifications
import com.ibunda.ilifeapps.databinding.ItemRvNotificationBinding
import com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.DetailActivity
import com.ibunda.ilifeapps.utils.loadImage

class NotificationsViewHolder (private val binding: ItemRvNotificationBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Notifications) {
        with(binding) {

            if (data.read == true) {
                linearNotif.setBackgroundResource(R.color.bgChatNotif)
            }
            imgProfile.loadImage(data.senderPicture)
            tvTitle.text = (data.title)
            tvBody.text = (data.body)
            tvDate.text = (data.date)

            with(itemView) {
                setOnClickListener {
                    val intent =
                        Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_ORDER_ID, data.orderId)
                    context.startActivity(intent)
                }
            }

        }
    }
}