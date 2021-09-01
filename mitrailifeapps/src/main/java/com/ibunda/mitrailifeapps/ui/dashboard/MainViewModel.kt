package com.ibunda.mitrailifeapps.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.mitrailifeapps.data.firebase.FirebaseServices
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.data.model.Orders

class MainViewModel: ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()
    private var _orderData = MutableLiveData<Orders>()
    private var _mitraProfile = MutableLiveData<Mitras>()

    //getMitraId
    fun setMitraProfile(mitraId: String): LiveData<Mitras> {
        _mitraProfile = firebaseServices.getMitraData(mitraId) as MutableLiveData<Mitras>
        return _mitraProfile
    }
    //Setting
    fun getProfileData(): LiveData<Mitras> {
        return _mitraProfile
    }

    //EditProfile
    fun editProfileMitra(authUser: Mitras): LiveData<Mitras> =
        firebaseServices.editMitraData(authUser)

    //Home
    fun getListOrderKhusus(orderKhusus: Boolean): LiveData<List<Orders>?> {
        return firebaseServices.getListOrderKhususData(orderKhusus, "orders").asLiveData()
    }
    fun setOrderData(orderId: String): LiveData<Orders> {
        _orderData = firebaseServices.getOrdersData(orderId) as MutableLiveData<Orders>
        return _orderData
    }
    fun getOrderKhususData(): LiveData<Orders> {
        return _orderData
    }

    //Order


}