package com.ibunda.mitrailifeapps.ui.dashboard.chats.chatmessages

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.data.model.ChatMessages
import com.ibunda.mitrailifeapps.databinding.ItemRvChatMessagesLeftBinding
import com.ibunda.mitrailifeapps.databinding.ItemRvChatMessagesRightBinding
import com.ibunda.mitrailifeapps.databinding.ItemRvChatMessagesTawaranLeftBinding
import com.ibunda.mitrailifeapps.databinding.ItemRvChatMessagesTawaranRightBinding

class ChatMessagesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listMessages = ArrayList<ChatMessages>()
    private var shopId: String? = null
    private var oldDate: String? = null

    fun setListChatMessages(chatMessages: List<ChatMessages>?, shopId: String?) {
        if (chatMessages == null) return
        this.shopId = shopId
        this.listMessages.clear()
        this.listMessages.addAll(chatMessages)
        oldDate = null
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TAWARAN_RIGHT -> {
                val viewholder =
                    ItemRvChatMessagesTawaranRightBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ChatMessagesTawaranRightViewHolder(viewholder)
            }
            VIEW_TAWARAN_LEFT -> {
                val viewholder =
                    ItemRvChatMessagesTawaranLeftBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ChatMessagesTawaranLeftViewHolder(viewholder)
            }
            VIEW_MESSAGES_RIGHT -> {
                val viewholder =
                    ItemRvChatMessagesRightBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ChatMessagesRightViewHolder(viewholder)
            }
            else -> {
                val viewholder =
                    ItemRvChatMessagesLeftBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ChatMessagesLeftViewHolder(viewholder)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatMessages = listMessages[position]
        var showDate = chatMessages.date != oldDate
        oldDate = chatMessages.date
        Log.d(oldDate, "$showDate")
        if (chatMessages.tawar) {
            if (chatMessages.sender == shopId) {
                (holder as ChatMessagesTawaranRightViewHolder).bind(chatMessages, showDate)
            } else {
                (holder as ChatMessagesTawaranLeftViewHolder).bind(chatMessages, showDate)
            }
        } else if (chatMessages.sender == shopId) {
            (holder as ChatMessagesRightViewHolder).bind(chatMessages, showDate)
        } else {
            (holder as ChatMessagesLeftViewHolder).bind(chatMessages, showDate)
        }
    }

    override fun getItemCount(): Int = listMessages.size

    override fun getItemViewType(position: Int): Int {
        val data = listMessages[position]
        return if (data.tawar) { // put your condition, according to your requirements
            if (data.sender == shopId) {
                VIEW_TAWARAN_RIGHT
            } else {
                VIEW_TAWARAN_LEFT
            }
        } else if (data.sender == shopId) {
            VIEW_MESSAGES_RIGHT
        } else {
            VIEW_MESSAGES_LEFT
        }
    }

    companion object {
        const val VIEW_TAWARAN_LEFT = 0
        const val VIEW_TAWARAN_RIGHT = 1
        const val VIEW_MESSAGES_LEFT = 2
        const val VIEW_MESSAGES_RIGHT = 3
    }
}