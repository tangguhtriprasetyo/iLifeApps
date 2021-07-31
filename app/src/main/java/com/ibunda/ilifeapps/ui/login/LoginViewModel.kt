package com.ibunda.ilifeapps.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.Users

class LoginViewModel : ViewModel() {
    private val firebaseServices: FirebaseServices = FirebaseServices()

    fun signInWithGoogle(idToken: String): LiveData<Users> =
        firebaseServices.signInWithGoogle(idToken)

    fun createdNewUser(authUser: Users): LiveData<Users> =
        firebaseServices.createUserToFirestore(authUser)
}