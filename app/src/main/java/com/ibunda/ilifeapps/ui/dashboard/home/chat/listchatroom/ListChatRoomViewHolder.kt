package com.ibunda.ilifeapps.ui.dashboard.home.chat.listchatroom

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Chats
import com.ibunda.ilifeapps.databinding.ItemRvRoomChatBinding
import com.ibunda.ilifeapps.utils.loadImage

class ListChatRoomViewHolder (private val binding: ItemRvRoomChatBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Chats) {
        with(binding) {

            if (data.verified == true) {
                icVerified.visibility = View.VISIBLE
            }
            if (!data.read!!) {
                linearRoomChat.setBackgroundResource(R.color.bgChatNotif)
            }
            imgProfile.loadImage(data.shopPicture)
            tvNamaMitra.text = (data.shopName)
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