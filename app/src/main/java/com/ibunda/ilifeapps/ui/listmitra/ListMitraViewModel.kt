package com.ibunda.ilifeapps.ui.listmitra

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Users

class ListMitraViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()
    private var _shopData = MutableLiveData<Shops>()
    private var _userProfile = MutableLiveData<Users>()

    fun setShopData(shopId: String): LiveData<Shops> {
        _shopData = firebaseServices.getShopData(shopId) as MutableLiveData<Shops>
        return _shopData
    }

    fun getShopData(): LiveData<Shops> {
        return _shopData
    }

    fun setUserProfile(userId: String): LiveData<Users> {
        _userProfile = firebaseServices.getUserData(userId) as MutableLiveData<Users>
        return _userProfile
    }

    fun getProfileData(): LiveData<Users> {
        return _userProfile
    }

    val dataCategory = MutableLiveData<String>()

}