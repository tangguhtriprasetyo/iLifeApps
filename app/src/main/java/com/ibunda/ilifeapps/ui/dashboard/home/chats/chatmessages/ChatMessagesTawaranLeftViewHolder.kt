package com.ibunda.ilifeapps.ui.dashboard.home.chats.chatmessages

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.ChatMessages
import com.ibunda.ilifeapps.databinding.ItemRvChatMessagesTawaranLeftBinding

class ChatMessagesTawaranLeftViewHolder(
    private val binding: ItemRvChatMessagesTawaranLeftBinding,
    private val chatMessagesClickCallback: ChatMessagesClickCallback
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
            tvHargaTawar.text = data.message
            tvTimeTawar.text = data.time
            with(itemView) {
                setOnClickListener {
                    chatMessagesClickCallback.onItemClicked(data)
                }
            }
        }
    }
}