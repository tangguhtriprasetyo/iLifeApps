package com.ibunda.ilifeapps.ui.dashboard.home.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.ChatMessages
import com.ibunda.ilifeapps.data.model.ChatRoom
import com.ibunda.ilifeapps.data.model.Users

class ChatsViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()

    private val _userData = MutableLiveData<Users?>()
    val userData: LiveData<Users?> = _userData

    private val _chatRoom = MutableLiveData<ChatRoom?>()
    val chatRoom: LiveData<ChatRoom?> = _chatRoom

    fun getListChatRoom(userId: String): LiveData<List<ChatRoom>?> {
        return firebaseServices.getListChatRoom(userId).asLiveData()
    }

    fun getListChatMessages(chatRoomId: String): LiveData<List<ChatMessages>?> {
        return firebaseServices.getListChatMessages(chatRoomId).asLiveData()
    }

    fun updateChatRoom(chatRoom: ChatRoom): LiveData<String> =
        firebaseServices.sendTawaran(chatRoom)

    fun sendChat(chatRoomId: String, chatMessages: ChatMessages): LiveData<String> =
        firebaseServices.sendChat(chatRoomId, chatMessages)

    fun setUserData(userData: Users) {
        _userData.value = userData
    }

    fun setChatRoomId(chatRoom: ChatRoom) {
        _chatRoom.value = chatRoom
    }
}