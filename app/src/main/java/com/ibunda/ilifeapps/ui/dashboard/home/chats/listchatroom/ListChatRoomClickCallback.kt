package com.ibunda.ilifeapps.ui.dashboard.home.chats.listchatroom

import com.ibunda.ilifeapps.data.model.ChatRoom

interface ListChatRoomClickCallback {
    fun onItemClicked(data: ChatRoom)
}