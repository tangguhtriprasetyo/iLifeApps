package com.ibunda.ilifeapps.data.firebase

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.ibunda.ilifeapps.data.model.Ads
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.utils.AppConstants.STATUS_SUCCESS
import com.ibunda.ilifeapps.utils.DateHelper.getCurrentDate
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class FirebaseServices {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestoreRef: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val usersRef: CollectionReference = firestoreRef.collection("users")
    private val shopsRef: CollectionReference = firestoreRef.collection("shops")
    private val ordersRef: CollectionReference = firestoreRef.collection("orders")

    private var STATUS_ERROR = "error"

    fun createUserToFirestore(authUser: Users): LiveData<Users> {
        val createdUserData = MutableLiveData<Users>()
        CoroutineScope(IO).launch {
            val docRef: DocumentReference = usersRef.document(authUser.userId.toString())
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document: DocumentSnapshot? = task.result
                    if (document?.exists() == false) {
                        docRef.set(authUser).addOnCompleteListener {
                            if (it.isSuccessful) {
                                authUser.isCreated = true
                                authUser.isNew = false
                                authUser.registeredToken = null
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

    fun uploadOrder(orders: Orders): LiveData<String> {
        val statusOrder = MutableLiveData<String>()
        CoroutineScope(IO).launch {
            val docRef: DocumentReference = ordersRef.document(orders.orderId!!)
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document: DocumentSnapshot? = task.result
                    if (document?.exists() == false) {
                        docRef.set(orders).addOnCompleteListener {
                            if (it.isSuccessful) {
                                usersRef.document(orders.userId.toString())
                                    .update("totalOrder", FieldValue.increment(1))
                                    .addOnCompleteListener {
                                        statusOrder.postValue(STATUS_SUCCESS)
                                    }
                                    .addOnFailureListener { error ->
                                        STATUS_ERROR = error.message.toString()
                                        statusOrder.postValue(STATUS_ERROR)
                                        Log.d("ErrorUpdateTotalOrder: ", error.message.toString())
                                    }
                            } else {
                                STATUS_ERROR = it.exception?.message.toString()
                                statusOrder.postValue(STATUS_ERROR)
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
                    STATUS_ERROR = it.message.toString()
                    statusOrder.postValue(STATUS_ERROR)
                    Log.d("ErrorUploadOrder: ", it.message.toString())
                }
        }


        return statusOrder
    }

    fun getOrderData(orderId: String): LiveData<Orders> {
        val docRef: DocumentReference = ordersRef.document(orderId)
        val orderData = MutableLiveData<Orders>()
        CoroutineScope(IO).launch {
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val userProfile = document.toObject<Orders>()
                    orderData.postValue(userProfile!!)
                    Log.d("getUserProfile: ", userProfile.toString())
                } else {
                    Log.d("Error getting Doc", "Document Doesn't Exist")
                }
            }
                .addOnFailureListener {

                }
        }
        return orderData
    }

    fun updateOrderData(orderData: Orders): LiveData<Orders> {
        val editOrderData = MutableLiveData<Orders>()
        CoroutineScope(IO).launch {
            val docRef: DocumentReference = ordersRef.document(orderData.orderId.toString())
            docRef.set(orderData, SetOptions.merge()).addOnCompleteListener {
                if (it.isSuccessful) {
                    editOrderData.postValue(orderData)
                } else {
                    Log.d(
                        "errorUpdateOrder: ",
                        it.exception?.message.toString()
                    )
                }
            }
                .addOnFailureListener {
                    Log.d(
                        "errorCreateUser: ", it.message.toString()
                    )
                }
        }
        return editOrderData
    }

    fun signInWithGoogleFacebook(idToken: AuthCredential): LiveData<Users> {
        val authenticatedUser = MutableLiveData<Users>()
        CoroutineScope(IO).launch {
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
                                phone = phone,
                                isNew = isNewUser,
                                totalOrder = 0,
                                tanggalDibuat = getCurrentDate()
                            )
                            authenticatedUser.postValue(userInfo)
                        }
                    } else {
                        Log.d("Error Authentication", "signInWithGoogle: ", task.exception)
                        val errorMessage = Users(
                            errorMessage = task.exception?.message
                        )
                        authenticatedUser.postValue(errorMessage)
                    }
                }
        }
        return authenticatedUser
    }

    fun getUserData(userId: String): LiveData<Users> {
        val docRef: DocumentReference = usersRef.document(userId)
        val userProfileData = MutableLiveData<Users>()
        CoroutineScope(IO).launch {
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val userProfile = document.toObject<Users>()
                    userProfileData.postValue(userProfile!!)
                    Log.d("getUserProfile: ", userProfile.toString())
                } else {
                    Log.d("Error getting Doc", "Document Doesn't Exist")
                }
            }
                .addOnFailureListener {

                }
        }
        return userProfileData
    }

    fun editUserData(authUser: Users): LiveData<Users> {
        val editedUserData = MutableLiveData<Users>()
        CoroutineScope(IO).launch {
            val docRef: DocumentReference = usersRef.document(authUser.userId.toString())
            docRef.set(authUser, SetOptions.merge()).addOnCompleteListener {
                if (it.isSuccessful) {
                    authUser.isNew = false
                    editedUserData.postValue(authUser)
                } else {
                    Log.d(
                        "errorUpdateProfile: ",
                        it.exception?.message.toString()
                    )
                }
            }
                .addOnFailureListener {
                    Log.d(
                        "errorCreateUser: ", it.message.toString()
                    )
                }
        }
        return editedUserData
    }

    fun uploadFiles(uri: Uri, uid: String, type: String, name: String): LiveData<String> {
        val mStorage: FirebaseStorage = Firebase.storage
        val storageRef = mStorage.reference
        val fileRef = storageRef.child("$uid/$type/$name")
        val downloadUrl = MutableLiveData<String>()

        fileRef.putFile(uri).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            fileRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                downloadUrl.postValue(downloadUri.toString())
                Log.d("uploadFiles: ", downloadUri.toString())
            } else {
                task.exception?.let {
                    throw it
                }
            }
        }
        return downloadUrl
    }

    fun getShopData(shopId: String): LiveData<Shops> {
        val docRef: DocumentReference = shopsRef.document(shopId)
        val shopData = MutableLiveData<Shops>()
        CoroutineScope(IO).launch {
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val shopsData = document.toObject<Shops>()
                    shopData.postValue(shopsData!!)
                    Log.d("getShopData: ", shopsData.toString())
                } else {
                    Log.d("Error getting Doc", "Document Doesn't Exist")
                }
            }
                .addOnFailureListener {

                }
        }
        return shopData
    }

    fun getListShopData(query: String, collectionRef: String): Flow<List<Shops>?> {

        return callbackFlow {

            val collectionRef: CollectionReference = firestoreRef.collection(collectionRef)
            val listenerRegistration =
                collectionRef.whereEqualTo("categoryName", query)
                    .addSnapshotListener { querySnapshot: QuerySnapshot?, firestoreException: FirebaseFirestoreException? ->
                        if (firestoreException != null) {
                            cancel(
                                message = "Error fetching posts",
                                cause = firestoreException
                            )
                            return@addSnapshotListener
                        }
                        val listShops = querySnapshot?.documents?.mapNotNull {
                            it.toObject<Shops>()
                        }
                        offer(listShops)
                        Log.d("Shops", listShops.toString())
                    }
            awaitClose {
                Log.d(TAG, "getListShops: ")
                listenerRegistration.remove()
            }
        }

    }

    fun getListOrdersData(
        query: String,
        userId: String,
        collectionRef: String
    ): Flow<List<Orders>?> {

        return callbackFlow {

            val collectionRef: CollectionReference = firestoreRef.collection(collectionRef)
            Log.d(TAG, "getListOrdersData: $collectionRef, $query, $userId")
            val listenerRegistration =
                collectionRef.whereEqualTo("status", query)
                    .whereEqualTo("userId", userId)
                    .addSnapshotListener { querySnapshot: QuerySnapshot?, firestoreException: FirebaseFirestoreException? ->
                        if (firestoreException != null) {
                            cancel(
                                message = "Error fetching posts",
                                cause = firestoreException
                            )
                            return@addSnapshotListener
                        }
                        val listOrders = querySnapshot?.documents?.mapNotNull {
                            it.toObject<Orders>()
                        }
                        offer(listOrders)
                        Log.d("Orders", listOrders.toString())
                    }
            awaitClose {
                Log.d(TAG, "getListOrders: ")
                listenerRegistration.remove()
            }
        }

    }

    fun getListAdsHome(query: String, collectionRef: String): Flow<List<Ads>?> {

        return callbackFlow {

            val collectionRef: CollectionReference = firestoreRef.collection(collectionRef)
            val listenerRegistration =
                collectionRef.whereEqualTo("category", query)
                    .addSnapshotListener { querySnapshot: QuerySnapshot?, firestoreException: FirebaseFirestoreException? ->
                        if (firestoreException != null) {
                            cancel(
                                message = "Error fetching posts",
                                cause = firestoreException
                            )
                            return@addSnapshotListener
                        }
                        val listAds = querySnapshot?.documents?.mapNotNull {
                            it.toObject<Ads>()
                        }
                        offer(listAds)
                        Log.d("Ads", listAds.toString())
                    }
            awaitClose {
                Log.d(TAG, "getListAds: ")
                listenerRegistration.remove()
            }
        }

    }

    fun getListPromoShop(query: Boolean, collectionRef: String): Flow<List<Shops>?> {

        return callbackFlow {

            val collectionRef: CollectionReference = firestoreRef.collection(collectionRef)
            val listenerRegistration =
                collectionRef.whereEqualTo("promo", query)
                    .addSnapshotListener { querySnapshot: QuerySnapshot?, firestoreException: FirebaseFirestoreException? ->
                        if (firestoreException != null) {
                            cancel(
                                message = "Error fetching posts",
                                cause = firestoreException
                            )
                            return@addSnapshotListener
                        }
                        val listAds = querySnapshot?.documents?.mapNotNull {
                            it.toObject<Shops>()
                        }
                        offer(listAds)
                        Log.d("Ads", listAds.toString())
                    }
            awaitClose {
                Log.d(TAG, "getListAds: ")
                listenerRegistration.remove()
            }
        }

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

    fun updateChat() {

    }

    fun uploadNotification() {

    }

    fun uploadUlasan() {

    }


    fun uploadTawar() {

    }

    fun deleteNotification() {

    }

}