package com.ibunda.ilifeapps.ui.listmitra.listshop

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.Shops
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ListShopViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()

    fun getListShop(categoryName: String): LiveData<List<Shops>?> {
        return firebaseServices.getListShopData(categoryName, "shops").asLiveData()
    }

    fun getListPromoShop(promo: Boolean): LiveData<List<Shops>?> {
        return firebaseServices.getListPromoShop(promo, "shops").asLiveData()
    }


}