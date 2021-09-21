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
import com.ibunda.ilifeapps.data.model.*
import com.ibunda.ilifeapps.utils.AppConstants.STATUS_SUCCESS
import com.ibunda.ilifeapps.utils.DateHelper.getCurrentDate
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*

@ExperimentalCoroutinesApi
class FirebaseServices {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestoreRef: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val usersRef: CollectionReference = firestoreRef.collection("users")
    private val shopsRef: CollectionReference = firestoreRef.collection("shops")
    private val ordersRef: CollectionReference = firestoreRef.collection("orders")
    private val ulasanRef: CollectionReference = firestoreRef.collection("ulasan")
    private val notifRef: CollectionReference = firestoreRef.collection("notifications")
    private val chatRef: CollectionReference = firestoreRef.collection("chatRoom")

    private var STATUS_ERROR = "error"


    //<<<<<<<<<<<<<<<<<<<<<<<<<< AUTHENTICATION METHOD >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
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

    //<<<<<<<<<<<<<<<<<<<<<<<<<< GET DATA FROM DATABASE METHOD >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
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

    fun getNotifications(userId: String): Flow<List<Notifications>?> {
        return callbackFlow {

            val listenerRegistration =
                notifRef.whereEqualTo("receiverId", userId)
                    .addSnapshotListener { querySnapshot: QuerySnapshot?, firestoreException: FirebaseFirestoreException? ->
                        if (firestoreException != null) {
                            cancel(
                                message = "Error fetching posts",
                                cause = firestoreException
                            )
                            return@addSnapshotListener
                        }
                        val listNotif = querySnapshot?.documents?.mapNotNull {
                            it.toObject<Notifications>()
                        }
                        offer(listNotif)
                        Log.d("Notif", listNotif.toString())
                    }
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<< GET LIST DATA FROM DATABASE METHOD >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    fun getListShopData(
        query: String?,
        collectionRef: String,
        promo: Boolean,
        search: String?
    ): Flow<List<Shops>?> {

        return callbackFlow {

            val reference: CollectionReference = firestoreRef.collection(collectionRef)
            val listenerQuery = if (promo || query == null) {
                reference.whereEqualTo("promo", promo)
            } else if (search != null) {
                reference.whereEqualTo("shopName", search)
            } else {
                reference.whereEqualTo("categoryName", query)
            }

            val listenerRegistration =
                listenerQuery.addSnapshotListener { querySnapshot: QuerySnapshot?, firestoreException: FirebaseFirestoreException? ->
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

            val reference: CollectionReference = firestoreRef.collection(collectionRef)
            Log.d(TAG, "getListOrdersData: $collectionRef, $query, $userId")
            val listenerRegistration =
                reference.whereEqualTo("status", query)
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

    fun getListTawaranMitra(
        orderId: String
    ): Flow<List<Shops>?> {

        return callbackFlow {

            val reference: CollectionReference =
                firestoreRef.collection("orders").document(orderId).collection("listMitra")
            val listenerRegistration =
                reference.addSnapshotListener { querySnapshot: QuerySnapshot?, firestoreException: FirebaseFirestoreException? ->
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

    fun getListAdsHome(query: String, collectionRef: String): Flow<List<Ads>?> {

        return callbackFlow {

            val reference: CollectionReference = firestoreRef.collection(collectionRef)
            val listenerRegistration =
                reference.whereEqualTo("category", query)
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

    fun getListUlasan(role: String, query: String, collectionRef: String): Flow<List<Ulasan>?> {

        return callbackFlow {

            val reference: CollectionReference = firestoreRef.collection(collectionRef)
            val listenerRegistration =
                reference.whereEqualTo(role, query)
                    .addSnapshotListener { querySnapshot: QuerySnapshot?, firestoreException: FirebaseFirestoreException? ->
                        if (firestoreException != null) {
                            cancel(
                                message = "Error fetching posts",
                                cause = firestoreException
                            )
                            return@addSnapshotListener
                        }
                        val listShops = querySnapshot?.documents?.mapNotNull {
                            it.toObject<Ulasan>()
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

    fun getListChatRoom(userId: String, checkRead: Boolean): Flow<List<ChatRoom>?> {
        return callbackFlow {
            val query = if (checkRead) {
                chatRef.whereEqualTo("userId", userId).whereEqualTo("readByUser", false)
            } else {
                chatRef.whereEqualTo("userId", userId)
            }

            val listenerRegistration =
                query.addSnapshotListener { querySnapshot: QuerySnapshot?, firestoreException: FirebaseFirestoreException? ->
                    if (firestoreException != null) {
                        cancel(
                            message = "Error fetching posts",
                            cause = firestoreException
                        )
                        return@addSnapshotListener
                    }
                    val listChat = querySnapshot?.documents?.mapNotNull {
                        it.toObject<ChatRoom>()
                    }
                    offer(listChat)
                    Log.d("ListChatRoom", listChat.toString())
                }
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    fun getListChatMessages(chatRoomId: String): Flow<List<ChatMessages>?> {
        return callbackFlow {

            val listenerRegistration =
                chatRef.document(chatRoomId).collection("chats").orderBy("timeStamp", Query.Direction.ASCENDING)
                    .addSnapshotListener { querySnapshot: QuerySnapshot?, firestoreException: FirebaseFirestoreException? ->
                        if (firestoreException != null) {
                            cancel(
                                message = "Error fetching posts",
                                cause = firestoreException
                            )
                            return@addSnapshotListener
                        }
                        val listChat = querySnapshot?.documents?.mapNotNull {
                            it.toObject<ChatMessages>()
                        }
                        offer(listChat)
                        Log.d("Chats", listChat.toString())
                    }
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<< UPDATE DATABASE METHOD >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
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

    fun updateChat(chatRoomId: String): LiveData<String> {
        val status = MutableLiveData<String>()
        CoroutineScope(IO).launch {
            chatRef.document(chatRoomId)
                .update("readByUser", true)
                .addOnCompleteListener {
                    status.postValue(STATUS_SUCCESS)
                }
                .addOnFailureListener { error ->
                    STATUS_ERROR = error.message.toString()
                    status.postValue(STATUS_ERROR)
                    Log.d("ErrorUpdateTotalOrder: ", error.message.toString())
                }
        }
        return status
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<< POST DATA TO DATABASE METHOD >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
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

    fun uploadUlasan(ulasan: Ulasan, rating: Double): LiveData<String> {
        val statusOrder = MutableLiveData<String>()
        CoroutineScope(IO).launch {
            val docRef: DocumentReference = ulasanRef.document()
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document: DocumentSnapshot? = task.result
                    if (document?.exists() == false) {
                        docRef.set(ulasan).addOnCompleteListener {
                            if (it.isSuccessful) {
                                shopsRef.document(ulasan.shopId.toString())
                                    .update(
                                        "totalUlasan",
                                        FieldValue.increment(1),
                                        "rating",
                                        rating
                                    )
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

    fun uploadNotification(notif: Notifications): LiveData<String> {
        val statusNotif = MutableLiveData<String>()
        CoroutineScope(IO).launch {
            val docRef: DocumentReference = notifRef.document()
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document: DocumentSnapshot? = task.result
                    if (document?.exists() == false) {
                        docRef.set(notif).addOnCompleteListener {
                            if (it.isSuccessful) {
                                statusNotif.postValue(STATUS_SUCCESS)
                            } else {
                                STATUS_ERROR = it.exception?.message.toString()
                                statusNotif.postValue(STATUS_ERROR)
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
                    statusNotif.postValue(STATUS_ERROR)
                    Log.d("ErrorUploadNotif: ", it.message.toString())
                }
        }
        return statusNotif
    }

    fun sendTawaran(chatRoom: ChatRoom): LiveData<String> {
        val statusChat = MutableLiveData<String>()
        CoroutineScope(IO).launch {
            val docRef: DocumentReference = chatRef.document(chatRoom.chatRoomId!!)
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    docRef.set(chatRoom, SetOptions.merge()).addOnCompleteListener {
                        if (it.isSuccessful) {
                            statusChat.postValue(
                                STATUS_SUCCESS
                            )
                        } else {
                            STATUS_ERROR = it.exception?.message.toString()
                            statusChat.postValue(STATUS_ERROR)
                            Log.d(
                                "errorCreateUser: ",
                                it.exception?.message.toString()
                            )
                        }
                    }
                }
            }
                .addOnFailureListener {
                    STATUS_ERROR = it.message.toString()
                    statusChat.postValue(STATUS_ERROR)
                    Log.d("ErrorUploadNotif: ", it.message.toString())
                }
        }
        return statusChat
    }

    fun sendChat(chatRoomId: String, chatMessages: ChatMessages): LiveData<String> {
        val statusChat = MutableLiveData<String>()
        CoroutineScope(IO).launch {
            val docRef: DocumentReference =
                chatRef.document(chatRoomId).collection("chats").document()
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document: DocumentSnapshot? = task.result
                    if (document?.exists() == false) {
                        docRef.set(chatMessages).addOnCompleteListener {
                            if (it.isSuccessful) {
                                statusChat.postValue(STATUS_SUCCESS)
                            } else {
                                STATUS_ERROR = it.exception?.message.toString()
                                statusChat.postValue(STATUS_ERROR)
                            }
                        }
                    }
                }
            }
                .addOnFailureListener {
                    STATUS_ERROR = it.message.toString()
                    statusChat.postValue(STATUS_ERROR)
                }
        }
        return statusChat
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<< DELETE DATA FROM DATABASE METHOD >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    fun deleteNotification(notifId: String): LiveData<String> {
        val statusNotif = MutableLiveData<String>()
        CoroutineScope(IO).launch {
            val docRef: DocumentReference = notifRef.document(notifId)
            docRef.delete().addOnSuccessListener { statusNotif.postValue(STATUS_SUCCESS) }
                .addOnFailureListener { statusNotif.postValue(it.message) }
        }
        return statusNotif
    }
}