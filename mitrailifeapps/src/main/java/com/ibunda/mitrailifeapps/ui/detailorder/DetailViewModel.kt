package com.ibunda.mitrailifeapps.ui.detailorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibunda.mitrailifeapps.data.firebase.FirebaseServices
import com.ibunda.mitrailifeapps.data.model.Orders
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DetailViewModel: ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()
    private var _orderData = MutableLiveData<Orders>()

    fun setOrderData(orderId: String): LiveData<Orders> {
        _orderData = firebaseServices.getOrdersData(orderId) as MutableLiveData<Orders>
        return _orderData
    }

    fun getOrderData(): LiveData<Orders> {
        return _orderData
    }

    fun updateOrderData(orderId: Orders): LiveData<Orders> =
        firebaseServices.updateOrderData(orderId)

}