package com.ibunda.mitrailifeapps.ui.dashboard.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.mitrailifeapps.data.firebase.FirebaseServices
import com.ibunda.mitrailifeapps.data.model.ChatMessages
import com.ibunda.mitrailifeapps.data.model.ChatRoom
import com.ibunda.mitrailifeapps.data.model.Shops
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ChatsViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()

    private val _shopData = MutableLiveData<Shops?>()
    val shopData: LiveData<Shops?> = _shopData

    private val _chatRoom = MutableLiveData<String?>()
    val chatRoom: LiveData<String?> = _chatRoom

    fun getListChatRoom(shopId: String): LiveData<List<ChatRoom>?> {
        return firebaseServices.getListChatRoom(shopId, false).asLiveData()
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

    fun setShopData(shopData: Shops) {
        _shopData.value = shopData
    }

    fun setChatRoomId(chatRoom: String) {
        _chatRoom.value = chatRoom
    }

    fun getChatRoomData(chatRoomId: String): LiveData<ChatRoom> =
        firebaseServices.getChatRoomData(chatRoomId)
}