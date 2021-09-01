package com.ibunda.mitrailifeapps.ui.detailorder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var ordersData: Orders

    companion object {
        const val PESANAN_FRAGMENT_TAG = "pesanan_fragment_tag"
        const val DIPROSES_FRAGMENT_TAG = "diproses_fragment_tag"
        const val SELESAI_FRAGMENT_TAG = "selesai_fragment_tag"
        const val DIBATALKAN_FRAGMENT_TAG = "dibatalkan_fragment_tag"
        const val EXTRA_ORDER = "extra_order"
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordersData = intent.getParcelableExtra<Orders>(EXTRA_ORDER) as Orders

    }

    private fun setCurrentFragment(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.commit {
            replace(R.id.host_detail_activity, fragment, fragmentTag)
        }
    }

}