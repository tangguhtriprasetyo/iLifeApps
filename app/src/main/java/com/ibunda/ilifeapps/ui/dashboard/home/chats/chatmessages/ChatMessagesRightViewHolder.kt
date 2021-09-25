package com.ibunda.ilifeapps.ui.dashboard.home.chats.chatmessages

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.ChatMessages
import com.ibunda.ilifeapps.databinding.ItemRvChatMessagesRightBinding

class ChatMessagesRightViewHolder(
    private val binding: ItemRvChatMessagesRightBinding
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