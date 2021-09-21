package com.ibunda.mitrailifeapps.ui.dashboard.chats.chatmessages

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.data.model.ChatMessages
import com.ibunda.mitrailifeapps.databinding.ItemRvChatMessagesLeftBinding

class ChatMessagesLeftViewHolder(
    private val binding: ItemRvChatMessagesLeftBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: ChatMessages, showDate: Boolean) {
        with(binding) {
            if (showDate) {
                //Visible Date View
                    tvDateMessage.visibility = View.VISIBLE
                tvDateMessage.text = data.date
            } else {
                tvDateMessage.visibility = View.GONE
            }
            tvChatMessage.text = data.message
            tvTimeChatSend.text = data.time
        }
    }
}