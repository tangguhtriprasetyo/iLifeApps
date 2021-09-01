package com.ibunda.mitrailifeapps.data.firebase

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.utils.DateHelper.getCurrentDate
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


@ExperimentalCoroutinesApi
class FirebaseServices {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestoreRef: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val shopsRef: CollectionReference = firestoreRef.collection("shops")
    private val ordersRef: CollectionReference = firestoreRef.collection("orders")
    private val mitraRef: CollectionReference = firestoreRef.collection("mitras")

    private var STATUS_ERROR = "error"

    fun createUserToFirestore(authUser: Mitras): LiveData<Mitras> {
        val createdMitraData = MutableLiveData<Mitras>()
        CoroutineScope(Dispatchers.IO).launch {
            val docRef: DocumentReference = mitraRef.document(authUser.mitraId.toString())
            docRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document: DocumentSnapshot? = task.result
                    if (document?.exists() == false) {
                        docRef.set(authUser).addOnCompleteListener {
                            if (it.isSuccessful) {
                                authUser.isCreated = true
                                authUser.isNew = false
                                authUser.registeredToken = null
                                createdMitraData.postValue(authUser)
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
        return createdMitraData
    }

    fun loginMitra(email: String?, password: String?) : LiveData<Mitras> {
        val authenticatedUser = MutableLiveData<Mitras>()
        CoroutineScope(Dispatchers.IO).launch {
            firebaseAuth.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val isNewUser = task.result?.additionalUserInfo?.isNewUser
                        val user: FirebaseUser? = firebaseAuth.currentUser
                        if (user != null) {
                            val uid = user.uid
                            val name = user.displayName
                            val email = user.email
                            val userInfo = Mitras(
                                mitraId = uid,
                                name = name,
                                email = email,
                                isNew = isNewUser,
                                totalShop = 0,
                                registeredAt = getCurrentDate()
                            )
                            authenticatedUser.postValue(userInfo)
                        }
                    } else {
                        Log.d("Error Authentication", "signInWithGoogle: ", task.exception)
                        val errorMessage = Mitras(
                            errorMessage = task.exception?.message
                        )
                        authenticatedUser.postValue(errorMessage)
                    }
                }
        }
        return authenticatedUser
    }

    fun getMitraData(mitraId: String): LiveData<Mitras> {
        val docRef: DocumentReference = mitraRef.document(mitraId)
        val mitraProfileData = MutableLiveData<Mitras>()
        CoroutineScope(Dispatchers.IO).launch {
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val mitraProfile = document.toObject<Mitras>()
                    mitraProfileData.postValue(mitraProfile!!)
                    Log.d("getMitraProfile: ", mitraProfile.toString())
                } else {
                    Log.d("Error getting Doc", "Document Doesn't Exist")
                }
            }
                .addOnFailureListener {

                }
        }
        return mitraProfileData
    }

    fun editMitraData(authUser: Mitras): LiveData<Mitras> {
        val editedUserData = MutableLiveData<Mitras>()
        CoroutineScope(Dispatchers.IO).launch {
            val docRef: DocumentReference = mitraRef.document(authUser.mitraId.toString())
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

    fun getListOrderKhususData(query: Boolean, collectionRef: String): Flow<List<Orders>?> {

        return callbackFlow {

            val collectionRef: CollectionReference = firestoreRef.collection(collectionRef)
            val listenerRegistration =
                collectionRef.whereEqualTo("orderKhusus", query)
                    .addSnapshotListener { querySnapshot: QuerySnapshot?, firestoreException: FirebaseFirestoreException? ->
                        if (firestoreException != null) {
                            cancel(
                                message = "Error fetching posts",
                                cause = firestoreException
                            )
                            return@addSnapshotListener
                        }
                        val listShops = querySnapshot?.documents?.mapNotNull {
                            it.toObject<Orders>()
                        }
                        offer(listShops)
                        Log.d("Shops", listShops.toString())
                    }
            awaitClose {
                Log.d(ContentValues.TAG, "getListShops: ")
                listenerRegistration.remove()
            }
        }

    }

    fun getOrdersData(orderId: String): LiveData<Orders> {
        val docRef: DocumentReference = ordersRef.document(orderId)
        val orderData = MutableLiveData<Orders>()
        CoroutineScope(Dispatchers.IO).launch {
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val ordersData = document.toObject<Orders>()
                    orderData.postValue(ordersData!!)
                    Log.d("getShopData: ", ordersData.toString())
                } else {
                    Log.d("Error getting Doc", "Document Doesn't Exist")
                }
            }
                .addOnFailureListener {

                }
        }
        return orderData
    }


}