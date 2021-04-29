package com.example.ilifeapps.ui.notifications

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ilifeapps.databinding.ActivityNotificationsBinding

class NotificationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}