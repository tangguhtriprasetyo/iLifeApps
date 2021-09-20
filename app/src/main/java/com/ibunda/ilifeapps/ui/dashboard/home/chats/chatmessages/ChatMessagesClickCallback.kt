package com.ibunda.ilifeapps.ui.dashboard.home.chats.chatmessages

import com.ibunda.ilifeapps.data.model.ChatMessages

interface ChatMessagesClickCallback {
    fun onItemClicked(data: ChatMessages)
}