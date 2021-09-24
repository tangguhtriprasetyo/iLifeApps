package com.ibunda.mitrailifeapps.ui.detailorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibunda.mitrailifeapps.data.firebase.FirebaseServices
import com.ibunda.mitrailifeapps.data.model.Notifications
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.data.model.Shops
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DetailViewModel: ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()

    private var _orderData = MutableLiveData<Orders>()
    private var _shopsProfile = MutableLiveData<Shops>()

    fun setOrderData(orderId: String): LiveData<Orders> {
        _orderData = firebaseServices.getOrdersData(orderId) as MutableLiveData<Orders>
        return _orderData
    }

    fun getOrderData(): LiveData<Orders> {
        return _orderData
    }

    fun updateOrderData(orderId: Orders): LiveData<Orders> =
        firebaseServices.updateOrderData(orderId)

    //setShopId
    fun setShopsProfile(shopId: String): LiveData<Shops> {
        _shopsProfile = firebaseServices.getShopsData(shopId) as MutableLiveData<Shops>
        return _shopsProfile
    }
    fun getShopData(): LiveData<Shops> {
        return _shopsProfile
    }
    //EditShopData
    fun updateTotalOrderShop(authUser: Shops): LiveData<Shops> =
        firebaseServices.editShopData(authUser)

    fun uploadNotif(notif: Notifications): LiveData<String> =
        firebaseServices.uploadNotification(notif)


}