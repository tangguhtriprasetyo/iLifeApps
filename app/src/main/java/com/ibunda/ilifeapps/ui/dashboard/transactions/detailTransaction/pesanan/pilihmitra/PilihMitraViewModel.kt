package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.pilihmitra

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.Notifications
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.data.model.Shops
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class PilihMitraViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()

    fun getListTawaranMitra(orderId: String): LiveData<List<Shops>?> {
        return firebaseServices.getListTawaranMitra(orderId).asLiveData()
    }

    fun updateOrderData(orderId: Orders): LiveData<Orders> =
        firebaseServices.updateOrderData(orderId)

    fun uploadNotif(notif: Notifications): LiveData<String> =
        firebaseServices.uploadNotification(notif)
}