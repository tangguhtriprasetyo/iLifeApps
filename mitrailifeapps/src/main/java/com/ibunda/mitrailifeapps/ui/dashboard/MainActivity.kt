package com.ibunda.mitrailifeapps.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.ActivityMainBinding
import com.ibunda.mitrailifeapps.ui.dashboard.home.HomeFragment
import com.ibunda.mitrailifeapps.ui.dashboard.order.OrderFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.ProfileFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.ulasan.UlasanFragment
import com.ibunda.mitrailifeapps.ui.dashboard.transaction.TransactionFragment
import com.ibunda.mitrailifeapps.ui.dashboard.verification.FragmentVerification
import com.ibunda.mitrailifeapps.ui.login.LoginActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.DateFormat
import java.util.*

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExit = false

    private lateinit var mitra: Mitras
    private lateinit var shops: Shops
    private val mainViewModel: MainViewModel by viewModels()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val firestoreRef: FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        const val HOME_FRAGMENT_TAG = "home_fragment_tag"
        const val ORDER_FRAGMENT_TAG = "order_fragment_tag"
        const val TRANSACTIONS_FRAGMENT_TAG = "transactions_fragment_tag"
        const val PROFILE_FRAGMENT_TAG = "profile_fragment_tag"
        const val VERIFICATION_FRAGMENT_TAG = "verification_fragment_tag"
        const val ULASAN_FRAGMENT_TAG = "ulasan_fragment_tag"
        const val CHILD_FRAGMENT = "child_fragment"
        const val EXTRA_USER = "extra_user"
        const val PREFS_NAME = "mitra_pref"
        const val EXTRA_SHOP = "extra_shop"
        const val EXTRA_TRANSACTION = "extra_ulasan"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val orderFragment = OrderFragment()
        val transactionsFragment = TransactionFragment()
        val profileFragment = ProfileFragment()
        val verificationFragment = FragmentVerification()

        if (intent.hasExtra(EXTRA_TRANSACTION)) {
            val shopId = intent.getStringExtra(EXTRA_SHOP)
            mainViewModel.setShopsProfile(shopId.toString()).observe(this, { shopsProfile ->
                if (shopsProfile != null) {
                    shops = shopsProfile
                    val ulasan = intent.getBooleanExtra(EXTRA_TRANSACTION, false)
                    val mUlasanFragment = UlasanFragment()
                    val mBundle = Bundle()
                    mBundle.putBoolean(UlasanFragment.FROM_TRANSACTION, ulasan)
                    mUlasanFragment.arguments = mBundle
                    setCurrentFragment(mUlasanFragment, ULASAN_FRAGMENT_TAG)
                }
            })

        } else {

            mitra = intent.getParcelableExtra<Mitras>(EXTRA_USER) as Mitras

            setMitraDataProfile()
            Log.e("savedInstanceFalse", "homeFragment")
            mainViewModel.setMitraProfile(mitra.mitraId.toString()).observe(this, { mitraProfile ->
                if (mitraProfile != null) {
                    mitra = mitraProfile
                    if (mitra.verified == true) {
                        setCurrentFragment(homeFragment, HOME_FRAGMENT_TAG)
                    } else {
                        setCurrentFragment(verificationFragment, VERIFICATION_FRAGMENT_TAG)
                    }
                }
            })
        }


        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.beranda -> {
                    setCurrentFragment(homeFragment, HOME_FRAGMENT_TAG)
                }
                R.id.pesanan -> {
                    setCurrentFragment(orderFragment, ORDER_FRAGMENT_TAG)
                }
                R.id.transaksi -> {
                    setCurrentFragment(transactionsFragment, TRANSACTIONS_FRAGMENT_TAG)
                }
                R.id.akun -> {
                    setCurrentFragment(profileFragment, PROFILE_FRAGMENT_TAG)
                }
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.commit {
            replace(R.id.host_fragment_activity_main, fragment, fragmentTag)
        }
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
            return
        } else if (supportFragmentManager.findFragmentByTag(CHILD_FRAGMENT) != null) {
            super.onBackPressed()
            binding.bottomNavigation.visibility = View.VISIBLE
            return
        }

        this.doubleBackToExit = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExit = false }, 2000)
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(this)
    }

    private fun setMitraDataProfile() {
        mainViewModel.setMitraProfile(mitra.mitraId.toString()).observe(this, { mitraProfile ->
            if (mitraProfile != null) {
                mitra = mitraProfile
                setShopData()
            }
        })
    }

    private fun setShopData() {
        val preferences = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val shopId = preferences.getString("shopId", "none")
        Log.e(shopId, "shopIdMain")
        if (!shopId.equals("none")) {
            mainViewModel.setShopsProfile(shopId.toString()).observe(this, { shopsProfile ->
                if (shopsProfile != null) {
                    shops = shopsProfile
                }
            })
        }
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(this)
    }

    override fun onAuthStateChanged(@NonNull firebaseAuth: FirebaseAuth) {
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
        if (firebaseUser == null) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initNotification() {

        firestoreRef.collection("mitras")
            .document(mitra.mitraId!!)
            .collection("Notifications")
            .addSnapshotListener(EventListener<QuerySnapshot?> { snapshots, e ->
                if (e != null) {
                    return@EventListener
                }
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MINUTE, -5)
                val before = calendar.time
                val calendar1 = Calendar.getInstance()
                val until = calendar1.time
                for (dc in snapshots!!.documentChanges) {
                    val dateFormat = DateFormat.getDateTimeInstance()
                    val date = dc.document.getDate("notificationDate")
                    if (dc.type == DocumentChange.Type.ADDED) {
                        if (!before.after(date) && !until.before(date)) {
                            Log.d("life", "Data: $date")
                            val title = dc.document.data["notificationTitle"].toString()
                            val body = dc.document.data["notificationBody"].toString()
//                            getNotification(title, body)
                            Log.e(title, "titleNotif")
                            Log.e(body, "bodyNotif")
                            Toast.makeText(
                                this@MainActivity,
                                "Notifikasi akan keluar",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
    }


}