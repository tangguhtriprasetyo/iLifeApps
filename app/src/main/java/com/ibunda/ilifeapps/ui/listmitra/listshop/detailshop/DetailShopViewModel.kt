package com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.Shops

class DetailShopViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()
    private var _shopData = MutableLiveData<Shops>()

    fun setUserProfile(shopId: String): LiveData<Shops> {
        _shopData = firebaseServices.getShopData(shopId) as MutableLiveData<Shops>
        return _shopData
    }

    fun getShopData(): LiveData<Shops> {
        return _shopData
    }

}