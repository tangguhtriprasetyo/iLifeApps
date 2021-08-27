package com.ibunda.mitrailifeapps.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibunda.mitrailifeapps.data.firebase.FirebaseServices
import com.ibunda.mitrailifeapps.data.model.Mitras

class MainViewModel: ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()
    private var _mitraProfile = MutableLiveData<Mitras>()

    fun setMitraProfile(mitraId: String): LiveData<Mitras> {
        _mitraProfile = firebaseServices.getMitraData(mitraId) as MutableLiveData<Mitras>
        return _mitraProfile
    }

    fun getProfileData(): LiveData<Mitras> {
        return _mitraProfile
    }

    fun editProfileMitra(authUser: Mitras): LiveData<Mitras> =
        firebaseServices.editMitraData(authUser)

}