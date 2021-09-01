package com.ibunda.ilifeapps.ui.dashboard

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.*

class MainViewModel : ViewModel() {
    private val firebaseServices: FirebaseServices = FirebaseServices()
    private var _userProfile = MutableLiveData<Users>()

    fun uploadOrder(orders: Orders): LiveData<String> =
        firebaseServices.uploadOrder(orders)

    fun getListAds(category: String): LiveData<List<Ads>?> {
        return firebaseServices.getListAdsHome(category, "ads").asLiveData()
    }

    fun setUserProfile(userId: String): LiveData<Users> {
        _userProfile = firebaseServices.getUserData(userId) as MutableLiveData<Users>
        return _userProfile
    }

    fun getProfileData(): LiveData<Users> {
        return _userProfile
    }

    fun editProfileUser(authUser: Users): LiveData<Users> =
        firebaseServices.editUserData(authUser)

    fun uploadImages(uri: Uri, uid: String, type: String, name: String): LiveData<String> =
        firebaseServices.uploadFiles(uri, uid, type, name)

    fun getListOtherShop(categoryName: String): LiveData<List<Shops>?> {
        return firebaseServices.getListShopData(categoryName, "shops").asLiveData()
    }

    fun getListPromoShop(promo: Boolean): LiveData<List<Shops>?> {
        return firebaseServices.getListPromoShop(promo, "shops").asLiveData()
    }

    //Ulasan
    fun getListUlasan(userId: String): LiveData<List<Ulasan>?> {
        return firebaseServices.getListUlasan("userId", userId, "ulasan").asLiveData()
    }

}