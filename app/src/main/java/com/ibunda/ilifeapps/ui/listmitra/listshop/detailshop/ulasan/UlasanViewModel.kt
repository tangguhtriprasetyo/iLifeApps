package com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.ulasan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.Ulasan
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UlasanViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()

    fun getListUlasan(shopId: String): LiveData<List<Ulasan>?> {
        return firebaseServices.getListUlasan("shopId", shopId, "ulasan").asLiveData()
    }

}