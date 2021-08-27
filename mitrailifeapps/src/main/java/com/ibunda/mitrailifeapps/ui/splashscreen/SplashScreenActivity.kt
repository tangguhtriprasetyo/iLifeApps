package com.ibunda.mitrailifeapps.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.databinding.ActivitySplashScreenBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainActivity
import com.ibunda.mitrailifeapps.ui.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {

    private val timeOut: Long = 2000
    private lateinit var binding: ActivitySplashScreenBinding

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var mitra = Mitras()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseUser = firebaseAuth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if (firebaseUser != null) {
                mitra.isAuthenticated = true
                mitra.mitraId = firebaseUser.uid
                mitra.name = firebaseUser.displayName
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_USER, mitra)
                startActivity(intent)
                finish()

            } else {
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, timeOut)



    }
}