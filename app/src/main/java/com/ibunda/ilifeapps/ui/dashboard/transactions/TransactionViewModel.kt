package com.ibunda.ilifeapps.ui.dashboard.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Ulasan
import com.ibunda.ilifeapps.data.model.Users

class TransactionViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()
    private var _orderData = MutableLiveData<Orders>()
    private var _shopData = MutableLiveData<Shops>()
    private var _userProfile = MutableLiveData<Users>()

    fun getListOrders(statusOrder: String, userId: String): LiveData<List<Orders>?> {
        return firebaseServices.getListOrdersData(statusOrder, userId, "orders").asLiveData()
    }

    fun setOrderData(orderId: String): LiveData<Orders> {
        _orderData = firebaseServices.getOrderData(orderId) as MutableLiveData<Orders>
        return _orderData
    }

    fun setShopData(shopId: String): LiveData<Shops> {
        _shopData = firebaseServices.getShopData(shopId) as MutableLiveData<Shops>
        return _shopData
    }

    fun getShopData(): LiveData<Shops> {
        return _shopData
    }

    fun uploadPenilaian(ulasan: Ulasan, rating: Double): LiveData<String> =
        firebaseServices.uploadUlasan(ulasan, rating)

    fun getOrderData(): LiveData<Orders> {
        return _orderData
    }

    fun updateOrderData(orderId: Orders): LiveData<Orders> =
        firebaseServices.updateOrderData(orderId)

    fun setUserProfile(userId: String): LiveData<Users> {
        _userProfile = firebaseServices.getUserData(userId) as MutableLiveData<Users>
        return _userProfile
    }

    fun getProfileData(): LiveData<Users> {
        return _userProfile
    }

}