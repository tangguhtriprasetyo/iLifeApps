package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ibunda.ilifeapps.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}