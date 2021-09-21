package com.ibunda.ilifeapps.ui.listmitra

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.*

class ListMitraViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()
    private var _shopData = MutableLiveData<Shops>()
    private var _userProfile = MutableLiveData<Users>()

    private val _dataCategory = MutableLiveData<String>()
    val dataCategory: LiveData<String> = _dataCategory

    fun setCategory(category: String) {
        _dataCategory.value = category
    }

    fun uploadOrder(orders: Orders): LiveData<String> =
        firebaseServices.uploadOrder(orders)

    fun uploadNotif(notif: Notifications): LiveData<String> =
        firebaseServices.uploadNotification(notif)

    fun uploadTawaran(chatRoom: ChatRoom): LiveData<String> =
        firebaseServices.sendTawaran(chatRoom)

    fun sendChat(chatRoomId: String, chatMessages: ChatMessages): LiveData<String> =
        firebaseServices.sendChat(chatRoomId, chatMessages)

    fun setShopData(shopId: String): LiveData<Shops> {
        _shopData = firebaseServices.getShopData(shopId) as MutableLiveData<Shops>
        return _shopData
    }

    fun getShopData(): LiveData<Shops> {
        return _shopData
    }

    fun setUserProfile(userId: String): LiveData<Users> {
        _userProfile = firebaseServices.getUserData(userId) as MutableLiveData<Users>
        return _userProfile
    }

    fun getProfileData(): LiveData<Users> {
        return _userProfile
    }

}