package com.ibunda.ilifeapps.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.Users

class LoginViewModel : ViewModel() {
    private val firebaseServices: FirebaseServices = FirebaseServices()

    fun signInWithGoogleFacebook(idToken: AuthCredential): LiveData<Users> =
        firebaseServices.signInWithGoogleFacebook(idToken)

    fun createdNewUser(authUser: Users): LiveData<Users> =
        firebaseServices.createUserToFirestore(authUser)

}