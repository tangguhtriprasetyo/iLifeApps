package com.ibunda.ilifeapps.ui.dashboard.home.chats.listchatroom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.ChatRoom
import com.ibunda.ilifeapps.databinding.ItemRvChatRoomBinding

class ListChatRoomAdapter(private val listChatRoomClickCallback: ListChatRoomClickCallback) :
    RecyclerView.Adapter<ListChatRoomViewHolder>() {
    private var listChatRoom = ArrayList<ChatRoom>()

    fun setListChatRoom(chatRoom: List<ChatRoom>?) {
        if (chatRoom == null) return
        this.listChatRoom.clear()
        this.listChatRoom.addAll(chatRoom)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListChatRoomViewHolder {
        val itemRvChatRoomBinding =
            ItemRvChatRoomBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ListChatRoomViewHolder(itemRvChatRoomBinding, listChatRoomClickCallback)
    }

    override fun onBindViewHolder(holder: ListChatRoomViewHolder, position: Int) {
        val chatRoom = listChatRoom[position]
        holder.bind(chatRoom)
    }

    override fun getItemCount(): Int = listChatRoom.size
}