package com.ibunda.mitrailifeapps.ui.dashboard

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.mitrailifeapps.data.firebase.FirebaseServices
import com.ibunda.mitrailifeapps.data.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainViewModel: ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()
    private var _orderData = MutableLiveData<Orders>()
    private var _mitraProfile = MutableLiveData<Mitras>()
    private var _shopsProfile = MutableLiveData<Shops>()
    private var _userProfile = MutableLiveData<Users>()

    //setMitraId
    fun setMitraProfile(mitraId: String): LiveData<Mitras> {
        _mitraProfile = firebaseServices.getMitraData(mitraId) as MutableLiveData<Mitras>
        return _mitraProfile
    }
    fun getProfileData(): LiveData<Mitras> {
        return _mitraProfile
    }

    //setShopId
    fun setShopsProfile(shopId: String): LiveData<Shops> {
        _shopsProfile = firebaseServices.getShopsData(shopId) as MutableLiveData<Shops>
        return _shopsProfile
    }
    fun getShopData(): LiveData<Shops> {
        return _shopsProfile
    }

    //createShops
    fun createdNewShop(shops: Shops): LiveData<Shops> =
            firebaseServices.createShops(shops)

    //EditMitraData
    fun editProfileMitra(authUser: Mitras): LiveData<Mitras> =
        firebaseServices.editMitraData(authUser)

    //Update Password Mitra
    fun updatePasswordMitra(email: String, recentPassword: String, newPassword: String): LiveData<String> =
        firebaseServices.updatePasswordMitra(email, recentPassword, newPassword)

    //EditShopData
    fun editShopData(authUser: Shops): LiveData<Shops> =
        firebaseServices.editShopData(authUser)

    //uploadImages
    fun uploadImages(uri: Uri, uid: String, type: String, name: String): LiveData<String> =
        firebaseServices.uploadFiles(uri, uid, type, name)

    //Home
    fun getListOrderKhusus(orderKhusus: Boolean, status: String, sort: Boolean, category: String): LiveData<List<Orders>?> {
        return firebaseServices.getListOrderKhususData(orderKhusus, status, sort, category, "orders").asLiveData()
    }

    //Detail Order Khusus
    fun setOrderData(orderId: String): LiveData<Orders> {
        _orderData = firebaseServices.getOrdersData(orderId) as MutableLiveData<Orders>
        return _orderData
    }
    fun getOrderData(): LiveData<Orders> {
        return _orderData
    }

    //ListOrder
    fun getListOrders(statusOrder: String, shopId: String): LiveData<List<Orders>?> {
        return firebaseServices.getListOrdersData(statusOrder, shopId, "orders").asLiveData()
    }

    //listShops
    fun getListShops(mitraId: String): LiveData<List<Shops>?> {
        return firebaseServices.getListShop(mitraId, "shops").asLiveData()
    }

    //listUlasan
    fun getListUlasan(shopId: String): LiveData<List<Ulasan>?> {
        return firebaseServices.getListUlasan(shopId, "ulasan").asLiveData()
    }

    //uploadTawaranJasa
    fun uploadTawaran(orderId: String, tawarId: String, offerOrder: OfferOrder): LiveData<String> =
        firebaseServices.uploadTawaranShop(orderId, tawarId, offerOrder)

    //
    fun getListNotif(shopId: String): LiveData<List<Notifications>?> {
        return firebaseServices.getNotifications(shopId).asLiveData()
    }

    fun getListChatRoom(shopId: String): LiveData<List<ChatRoom>?> {
        return firebaseServices.getListChatRoom(shopId, true).asLiveData()
    }

    //Notification
    fun deleteNotif(notifId: String): LiveData<String> =
        firebaseServices.deleteNotification(notifId)

    fun uploadNotif(notif: Notifications): LiveData<String> =
        firebaseServices.uploadNotification(notif)

    //Users
    fun setUserProfile(userId: String): LiveData<Users> {
        _userProfile = firebaseServices.getUserData(userId) as MutableLiveData<Users>
        return _userProfile
    }

    fun getUserProfileData(): LiveData<Users> {
        return _userProfile
    }

}