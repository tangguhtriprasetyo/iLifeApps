package com.ibunda.ilifeapps.ui.dashboard.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.Orders

class TransactionViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()

    fun getListOrders(statusOrder: String, userId: String): LiveData<List<Orders>?> {
        return firebaseServices.getListOrdersData(statusOrder, userId, "orders").asLiveData()
    }
}