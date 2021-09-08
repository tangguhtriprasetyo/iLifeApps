package com.ibunda.mitrailifeapps.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibunda.mitrailifeapps.data.firebase.FirebaseServices
import com.ibunda.mitrailifeapps.data.model.Mitras
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class LoginViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()
    private var _mitraProfile = MutableLiveData<Mitras>()

    fun createdNewUser(authUser: Mitras): LiveData<Mitras> =
        firebaseServices.createUserToFirestore(authUser)

    fun signInWithEmail(email: String, password: String): LiveData<Mitras> =
        firebaseServices.loginMitra(email, password)

    //setMitraId
    fun setMitraProfile(mitraId: String): LiveData<Mitras> {
        _mitraProfile = firebaseServices.getMitraData(mitraId) as MutableLiveData<Mitras>
        return _mitraProfile
    }
}