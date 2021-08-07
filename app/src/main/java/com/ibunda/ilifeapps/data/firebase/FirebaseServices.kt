package com.ibunda.ilifeapps.data.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.ibunda.ilifeapps.data.model.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseServices {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestoreRef: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val adsRef: CollectionReference = firestoreRef.collection("ads")
    private val chatRoomRef: CollectionReference = firestoreRef.collection("chatRoom")
    private val mitrasRef: CollectionReference = firestoreRef.collection("mitras")
    private val notificationsRef: CollectionReference = firestoreRef.collection("notifications")
    private val ordersRef: CollectionReference = firestoreRef.collection("orders")
    private val shopsRef: CollectionReference = firestoreRef.collection("shops")
    private val ulasanRef: CollectionReference = firestoreRef.collection("ulasan")
    private val usersRef: CollectionReference = firestoreRef.collection("users")

    fun createUserToFirestore(authUser: Users): LiveData<Users> {
        val createdUserData = MutableLiveData<Users>()
        CoroutineScope(Dispatchers.IO).launch {
            val docRef: DocumentReference = usersRef.document(authUser.userId.toString())
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document: DocumentSnapshot? = task.result
                    if (document?.exists() == false) {
                        docRef.set(authUser).addOnCompleteListener {
                            if (it.isSuccessful) {
                                authUser.isCreated = true
                                authUser.isNew = false
                                createdUserData.postValue(authUser)
                            } else {
                                Log.d(
                                    "errorCreateUser: ",
                                    it.exception?.message.toString()
                                )
                            }
                        }
                    }
                }
            }
                .addOnFailureListener {
                    Log.d("ErrorGetUser: ", it.message.toString())
                }
        }
        return createdUserData
    }

    fun signInWithGoogleFacebook(idToken: AuthCredential): LiveData<Users> {
        val authenticatedUser = MutableLiveData<Users>()
        CoroutineScope(Dispatchers.IO).launch {
            firebaseAuth.signInWithCredential(idToken)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val isNewUser = task.result?.additionalUserInfo?.isNewUser
                        val user: FirebaseUser? = firebaseAuth.currentUser
                        if (user != null) {
                            val uid = user.uid
                            val name = user.displayName
                            val email = user.email
                            val avatar = user.photoUrl
                            val phone = user.phoneNumber
                            val userInfo = Users(
                                userId = uid,
                                name = name,
                                email = email,
                                avatar = avatar.toString(),
                                phone = phone
                            )
                            userInfo.isNew = isNewUser
                            authenticatedUser.postValue(userInfo)
                        }
                    } else {
                        Log.d("Error Authentication", "signInWithGoogle: ", task.exception)
                    }
                }
        }
        return authenticatedUser
    }

    fun signInWithFacebook() {

    }

    fun signInOtp() {

    }

    fun getAdsHome() {

    }

    fun getStoryHome() {

    }

    fun getUserData() {

    }

    fun getListShop() {

    }

    fun getShopData() {

    }

    fun getNotifications() {

    }

    fun getListChat() {

    }

    fun getChatRoom() {

    }

    fun getUlasan() {
        //whereEqual
    }

    fun getListOrder() {
        //whereEqual
    }

    fun getDetailOrder() {

    }

    fun updateUserData() {

    }

    fun updateOrder() {

    }

    fun updateChat() {

    }

    fun uploadNotification() {

    }

    fun uploadUlasan() {

    }

    fun uploadOrder() {

    }

    fun uploadTawar() {

    }

    fun deleteNotification() {

    }

}