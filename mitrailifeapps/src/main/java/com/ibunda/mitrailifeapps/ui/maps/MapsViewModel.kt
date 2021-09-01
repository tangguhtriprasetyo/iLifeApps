package com.ibunda.mitrailifeapps.ui.maps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibunda.mitrailifeapps.data.firebase.FirebaseServices

class MapsViewModel: ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()

    val shopAdress = MutableLiveData<String>()
    val shopLatitude = MutableLiveData<Double>()
    val shopLongitude = MutableLiveData<Double>()

}