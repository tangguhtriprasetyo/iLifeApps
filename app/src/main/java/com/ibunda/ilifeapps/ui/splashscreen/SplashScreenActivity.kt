package com.ibunda.ilifeapps.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.ActivitySplashScreenBinding
import com.ibunda.ilifeapps.ui.dashboard.MainActivity
import com.ibunda.ilifeapps.ui.onboarding.OnboardingActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SplashScreenActivity : AppCompatActivity() {
    private val timeOut: Long = 2000
    private lateinit var binding: ActivitySplashScreenBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var user = Users()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseUser = firebaseAuth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if (firebaseUser != null) {
                user.isAuthenticated = true
                user.userId = firebaseUser.uid
                user.name = firebaseUser.displayName
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_USER, user)
                startActivity(intent)
                finish()

            } else {
                val intent = Intent(this@SplashScreenActivity, OnboardingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, timeOut)
    }
}