package com.ibunda.mitrailifeapps.ui.dashboard.home.chat.listchatroom

import androidx.recyclerview.widget.RecyclerView
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Chats
import com.ibunda.mitrailifeapps.databinding.ItemRvListChatBinding
import com.ibunda.mitrailifeapps.utils.loadImage

class ListChatRoomViewHolder (private val binding: ItemRvListChatBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Chats) {
        with(binding) {

            if (!data.read!!) {
                linearRoomChat.setBackgroundResource(R.color.bgChatNotif)
            }
            imgProfile.loadImage(data.imgUser)
            tvNamaMitra.text = (data.userName)
            tvChatMessage.text = (data.lastMessage)
            tvDateMessage.text = (data.lastDate)

            with(itemView) {
                setOnClickListener {
                    //
                }
            }

        }
    }
}