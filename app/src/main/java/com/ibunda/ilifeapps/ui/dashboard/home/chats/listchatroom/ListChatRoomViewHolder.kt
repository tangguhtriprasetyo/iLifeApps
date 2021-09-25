package com.ibunda.ilifeapps.ui.dashboard.home.chats.listchatroom

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.ChatRoom
import com.ibunda.ilifeapps.databinding.ItemRvChatRoomBinding
import com.ibunda.ilifeapps.utils.loadImage

class ListChatRoomViewHolder(
    private val binding: ItemRvChatRoomBinding,
    private val listChatRoomClickCallback: ListChatRoomClickCallback
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: ChatRoom) {
        with(binding) {

            if (data.verified) {
                icVerified.visibility = View.VISIBLE
            } else {
                icVerified.visibility = View.GONE
            }
            if (!data.readByUser) {
                linearRoomChat.setBackgroundResource(R.color.bgChatNotif)
            }
            imgProfile.loadImage(data.shopPicture)
            tvNamaMitra.text = (data.shopName)
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