package com.ibunda.mitrailifeapps.ui.dashboard.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.mitrailifeapps.data.firebase.FirebaseServices
import com.ibunda.mitrailifeapps.data.model.Orders
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TransactionViewModel: ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()

    fun getListOrders(statusOrder: String, shopId: String): LiveData<List<Orders>?> {
        return firebaseServices.getListOrdersData(statusOrder, shopId, "orders").asLiveData()
    }

}