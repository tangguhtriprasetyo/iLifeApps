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

    private val _chatRoomId = MutableLiveData<String?>()
    val chatRoomId: LiveData<String?> = _chatRoomId

    fun getListChatRoom(userId: String): LiveData<List<ChatRoom>?> {
        return firebaseServices.getListChatRoom(userId).asLiveData()
    }

    fun getListChatMessages(chatRoomId: String): LiveData<List<ChatMessages>?> {
        return firebaseServices.getListChatMessages(chatRoomId).asLiveData()
    }

    fun sendChat(chatRoom: ChatRoom, chatMessages: ChatMessages): LiveData<String> =
        firebaseServices.sendTawaran(chatRoom, chatMessages)

    fun setUserData(userData: Users) {
        _userData.value = userData
    }

    fun setChatRoomId(chatRoomId: String) {
        _chatRoomId.value = chatRoomId
    }
}