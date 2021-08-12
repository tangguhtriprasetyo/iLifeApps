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
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Users
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
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
                                phone = phone,
                                isNew = isNewUser
                            )
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

    fun getUserData(userId: String) : LiveData<Users> {
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

    fun getListShop(categoryName: String) : Flow<List<Shops>?> {

        return callbackFlow {
            val listenerRegistration =
                usersRef.whereEqualTo("categoryName", categoryName)
                    .addSnapshotListener { querySnapshot: QuerySnapshot?, firestoreException: FirebaseFirestoreException? ->
                        if (firestoreException != null) {
                            cancel(
                                message = "Error fetching posts",
                                cause = firestoreException
                            )
                            return@addSnapshotListener
                        }
                        val listFundedIdeas = querySnapshot?.documents?.mapNotNull {
                            it.toObject<Shops>()
                        }
                        offer(listFundedIdeas)
                        Log.d("FUNDEDIDEAS", listFundedIdeas.toString())
                    }
            awaitClose {
                Log.d(TAG, "getListIdeas: ")
                listenerRegistration.remove()
            }
        }

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