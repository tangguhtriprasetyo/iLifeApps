package com.ibunda.ilifeapps.ui.dashboard.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.Orders

class TransactionViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()
    private var _orderData = MutableLiveData<Orders>()

    fun getListOrders(statusOrder: String, userId: String): LiveData<List<Orders>?> {
        return firebaseServices.getListOrdersData(statusOrder, userId, "orders").asLiveData()
    }

    fun setOrderData(orderId: String): LiveData<Orders> {
        _orderData = firebaseServices.getOrderData(orderId) as MutableLiveData<Orders>
        return _orderData
    }

    fun getOrderData(): LiveData<Orders> {
        return _orderData
    }

}