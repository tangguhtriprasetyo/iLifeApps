package com.ibunda.ilifeapps.ui.dashboard.home.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.ChatMessages
import com.ibunda.ilifeapps.data.model.ChatRoom
import com.ibunda.ilifeapps.data.model.Users
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ChatsViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()

    private val _userData = MutableLiveData<Users?>()
    val userData: LiveData<Users?> = _userData

    private val _chatRoom = MutableLiveData<String?>()
    val chatRoom: LiveData<String?> = _chatRoom

    fun getListChatRoom(userId: String): LiveData<List<ChatRoom>?> {
        return firebaseServices.getListChatRoom(userId, false).asLiveData()
    }

    fun getListChatMessages(chatRoomId: String): LiveData<List<ChatMessages>?> {
        return firebaseServices.getListChatMessages(chatRoomId).asLiveData()
    }

    fun updateChatRoom(chatRoom: ChatRoom): LiveData<String> =
        firebaseServices.sendTawaran(chatRoom)

    fun readChat(chatRoomId: String): LiveData<String> =
        firebaseServices.updateChat(chatRoomId)

    fun sendChat(chatRoomId: String, chatMessages: ChatMessages): LiveData<String> =
        firebaseServices.sendChat(chatRoomId, chatMessages)

    fun setUserData(userData: Users) {
        _userData.value = userData
    }

    fun setChatRoomId(chatRoom: String) {
        _chatRoom.value = chatRoom
    }

    fun getChatRoomData(chatRoomId: String): LiveData<ChatRoom> =
        firebaseServices.getChatRoomData(chatRoomId)
}