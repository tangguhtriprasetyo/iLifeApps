package com.ibunda.ilifeapps.ui.listmitra

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.databinding.ActivityListMitraBinding
import com.ibunda.ilifeapps.ui.listmitra.listshop.ListShopFragment

class ListMitraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListMitraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMitraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listShopFragment = ListShopFragment()
        supportFragmentManager.commit {
            replace(R.id.host_listshop_activity, listShopFragment)
        }
    }
}