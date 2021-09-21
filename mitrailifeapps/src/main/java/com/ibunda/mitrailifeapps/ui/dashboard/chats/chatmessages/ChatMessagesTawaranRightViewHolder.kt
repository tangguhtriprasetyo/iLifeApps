package com.ibunda.mitrailifeapps.ui.dashboard.chats.chatmessages

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.data.model.ChatMessages
import com.ibunda.mitrailifeapps.databinding.ItemRvChatMessagesTawaranRightBinding

class ChatMessagesTawaranRightViewHolder(
    private val binding: ItemRvChatMessagesTawaranRightBinding
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

        }
    }
}