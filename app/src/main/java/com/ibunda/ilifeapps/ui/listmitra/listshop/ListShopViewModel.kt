package com.ibunda.ilifeapps.ui.listmitra.listshop

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.Shops
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
class ListShopViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()
    val lastVisibleItem = MutableStateFlow<Int>(0)

    fun getListShop(categoryName: String): LiveData<List<Shops>?> {
        return firebaseServices.getListShop(categoryName).asLiveData()
    }

}