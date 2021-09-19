package com.ibunda.ilifeapps.ui.dashboard.home.chats.chatmessages

import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.ChatMessages
import com.ibunda.ilifeapps.databinding.ItemRvChatMessagesBinding

class ChatMessagesViewHolder(private val binding: ItemRvChatMessagesBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: ChatMessages) {
        if (data.tawar) {
            initViewTawaran()
        } else {
            initViewText()
        }
    }

    private fun initViewText() {
        with(binding) {

        }
    }

    private fun initViewTawaran() {
        with(binding) {

        }
    }
}