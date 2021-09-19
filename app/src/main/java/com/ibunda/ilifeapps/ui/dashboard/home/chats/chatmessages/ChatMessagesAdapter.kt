package com.ibunda.ilifeapps.ui.dashboard.home.chats.chatmessages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.ChatMessages
import com.ibunda.ilifeapps.databinding.ItemRvChatMessagesBinding

class ChatMessagesAdapter : RecyclerView.Adapter<ChatMessagesViewHolder>() {
    private var listMessages = ArrayList<ChatMessages>()
    private var isTawaran = false

    fun setListChatMessages(chatMessages: List<ChatMessages>?) {
        if (chatMessages == null) return
        this.listMessages.clear()
        this.listMessages.addAll(chatMessages)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessagesViewHolder {

        val itemRvChatMessagesBinding =
            ItemRvChatMessagesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ChatMessagesViewHolder(itemRvChatMessagesBinding)

    }

    override fun onBindViewHolder(holder: ChatMessagesViewHolder, position: Int) {
        val chatMessages = listMessages[position]
        isTawaran = chatMessages.tawar
        holder.bind(chatMessages)
    }

    override fun getItemCount(): Int = listMessages.size
}