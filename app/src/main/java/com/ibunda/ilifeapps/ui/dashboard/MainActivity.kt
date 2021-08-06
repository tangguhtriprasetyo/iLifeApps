package com.ibunda.ilifeapps.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.databinding.ActivityMainBinding
import com.ibunda.ilifeapps.ui.dashboard.home.HomeFragment
import com.ibunda.ilifeapps.ui.dashboard.profile.ProfileFragment
import com.ibunda.ilifeapps.ui.dashboard.search.SearchFragment
import com.ibunda.ilifeapps.ui.dashboard.transactions.TransactionsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExit = false

    companion object {
        const val HOME_FRAGMENT_TAG = "home_fragment_tag"
        const val SEARCH_FRAGMENT_TAG = "search_fragment_tag"
        const val TRANSACTIONS_FRAGMENT_TAG = "transactions_fragment_tag"
        const val PROFILE_FRAGMENT_TAG = "profile_fragment_tag"
        const val CHILD_FRAGMENT = "child_fragment"
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()
        val transactionsFragment = TransactionsFragment()
        val profileFragment = ProfileFragment()
        if (savedInstanceState != null) {
            supportFragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG)
                ?.let { setCurrentFragment(it, HOME_FRAGMENT_TAG) }
        } else {

            setCurrentFragment(homeFragment, HOME_FRAGMENT_TAG)
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    setCurrentFragment(homeFragment, HOME_FRAGMENT_TAG)
                }
                R.id.search -> {
                    setCurrentFragment(searchFragment, SEARCH_FRAGMENT_TAG)
                }
                R.id.transactions -> {
                    setCurrentFragment(transactionsFragment, TRANSACTIONS_FRAGMENT_TAG)
                }
                R.id.profile -> {
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

}