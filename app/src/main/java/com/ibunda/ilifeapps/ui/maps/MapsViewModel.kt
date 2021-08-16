package com.ibunda.ilifeapps.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.Users

class MapsViewModel : ViewModel() {
    private val firebaseServices: FirebaseServices = FirebaseServices()

    fun editProfileUser(authUser: Users): LiveData<Users> =
        firebaseServices.editUserData(authUser)
}