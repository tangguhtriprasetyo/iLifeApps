package com.ibunda.mitrailifeapps.ui.dashboard.chats.listchatroom

import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.ChatRoom
import com.ibunda.mitrailifeapps.databinding.ItemRvListChatBinding
import com.ibunda.mitrailifeapps.utils.loadImage

class ListChatRoomViewHolder(
    private val binding: ItemRvListChatBinding,
    private val listChatRoomClickCallback: ListChatRoomClickCallback
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: ChatRoom) {
        with(binding) {

            if (!data.readByShop) {
                linearRoomChat.setBackgroundResource(R.color.bgChatNotif)
            }
            if (data.lastMessage == "null") {
                tvChatMessage.text = ""
            } else {
                tvChatMessage.text = (data.lastMessage)
            }
            imgProfile.loadImage(data.userPicture)
            tvNamaMitra.text = (data.userName)
            tvChatMessage.text = (data.lastMessage)
            tvDateMessage.text = (data.lastDate)

            with(itemView) {
                setOnClickListener {
                    listChatRoomClickCallback.onItemClicked(data)
                }
            }

        }
    }
}