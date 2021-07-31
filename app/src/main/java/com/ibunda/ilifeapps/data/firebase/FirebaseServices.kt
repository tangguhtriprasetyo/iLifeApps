package com.ibunda.ilifeapps.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

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

    fun signInWithGoogle() {

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