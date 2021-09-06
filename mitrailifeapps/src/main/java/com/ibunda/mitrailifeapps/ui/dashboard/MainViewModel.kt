package com.ibunda.mitrailifeapps.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.mitrailifeapps.data.firebase.FirebaseServices
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.data.model.Shops
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class MainViewModel: ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()
    private var _orderData = MutableLiveData<Orders>()
    private var _mitraProfile = MutableLiveData<Mitras>()
    private var _shopsProfile = MutableLiveData<Shops>()

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

    //EditProfile
    fun editProfileMitra(authUser: Mitras): LiveData<Mitras> =
        firebaseServices.editMitraData(authUser)

    //Home
    fun getListOrderKhusus(orderKhusus: Boolean, status: String): LiveData<List<Orders>?> {
        return firebaseServices.getListOrderKhususData(orderKhusus, status, "orders").asLiveData()
    }
    fun setOrderData(orderId: String): LiveData<Orders> {
        _orderData = firebaseServices.getOrdersData(orderId) as MutableLiveData<Orders>
        return _orderData
    }
    fun getOrderData(): LiveData<Orders> {
        return _orderData
    }

    //Order
    fun getListOrders(statusOrder: String, shopId: String): LiveData<List<Orders>?> {
        return firebaseServices.getListOrdersData(statusOrder, shopId, "orders").asLiveData()
    }

    fun updateOrderData(orderId: Orders): LiveData<Orders> =
        firebaseServices.updateOrderData(orderId)


}