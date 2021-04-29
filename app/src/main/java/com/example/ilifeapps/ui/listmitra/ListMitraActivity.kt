package com.example.ilifeapps.ui.listmitra

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ilifeapps.databinding.ActivityListMitraBinding

class ListMitraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListMitraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMitraBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}