package com.ibunda.mitrailifeapps.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.databinding.ActivityMainBinding
import com.ibunda.mitrailifeapps.ui.dashboard.home.HomeFragment
import com.ibunda.mitrailifeapps.ui.dashboard.order.OrderFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.ProfileFragment
import com.ibunda.mitrailifeapps.ui.dashboard.transaction.TransactionFragment
import com.ibunda.mitrailifeapps.ui.login.LoginActivity

class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExit = false

    private lateinit var mitra: Mitras
    private val mainViewModel: MainViewModel by viewModels()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        const val HOME_FRAGMENT_TAG = "home_fragment_tag"
        const val ORDER_FRAGMENT_TAG = "order_fragment_tag"
        const val TRANSACTIONS_FRAGMENT_TAG = "transactions_fragment_tag"
        const val PROFILE_FRAGMENT_TAG = "profile_fragment_tag"
        const val CHILD_FRAGMENT = "child_fragment"
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mitra = intent.getParcelableExtra<Mitras>(EXTRA_USER) as Mitras

        setMitraDataProfile()

        val homeFragment = HomeFragment()
        val orderFragment = OrderFragment()
        val transactionsFragment = TransactionFragment()
        val profileFragment = ProfileFragment()

        if (savedInstanceState != null) {
            supportFragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG)
                ?.let { setCurrentFragment(it, HOME_FRAGMENT_TAG) }
        } else {

            setCurrentFragment(homeFragment, HOME_FRAGMENT_TAG)
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
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
        setMitraDataProfile()
    }

    private fun setMitraDataProfile() {
        mainViewModel.setMitraProfile(mitra.mitraId.toString()).observe(this, { mitraProfile ->
            if (mitraProfile != null) {
                mitra = mitraProfile
            }
        })
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


}