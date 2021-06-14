package com.ibunda.ilifeapps.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ibunda.ilifeapps.databinding.ActivitySplashScreenBinding
import com.ibunda.ilifeapps.ui.onboarding.OnboardingActivity

class SplashScreenActivity : AppCompatActivity() {
    private val timeOut: Long = 2000
    private lateinit var binding: ActivitySplashScreenBinding
//    private val firebaseAuth = FirebaseAuth.getInstance()
//    private var user = UserEntity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val firebaseUser = firebaseAuth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
//            if (firebaseUser != null) {
//                user.isAuthenticated = true
//                user.uid = firebaseUser.uid
//                user.username = firebaseUser.displayName
//                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
//                intent.putExtra(MainActivity.EXTRA_USER, user)
//                startActivity(intent)
//                finish()
//
//            } else {
//                val intent = Intent(this@SplashScreenActivity, OnBoardingScreenActivity::class.java)
//                startActivity(intent)
//                finish()
//            }

            val intent = Intent(this@SplashScreenActivity, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        }, timeOut)
    }
}