package com.ibunda.mitrailifeapps.ui.dashboard.chats.listchatroom

import com.ibunda.mitrailifeapps.data.model.ChatRoom

interface ListChatRoomClickCallback {
    fun onItemClicked(data: ChatRoom)
}