package com.ibunda.ilifeapps.ui.dashboard.home.chats.chatmessages

import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.ChatMessages
import com.ibunda.ilifeapps.databinding.ItemRvChatMessagesRightBinding

class ChatMessagesRightViewHolder(
    private val binding: ItemRvChatMessagesRightBinding,
    private val chatMessagesClickCallback: ChatMessagesClickCallback
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: ChatMessages, showDate: Boolean) {
        with(binding) {
            if (showDate) {
                //Visible Date View
            } else {

            }
            with(itemView) {
                setOnClickListener {
                    chatMessagesClickCallback.onItemClicked(data)
                }
            }
        }
    }
}