package com.ibunda.mitrailifeapps.ui.detailorder

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.ActivityDetailBinding
import com.ibunda.mitrailifeapps.ui.detailorder.dibatalkan.DibatalkanFragment
import com.ibunda.mitrailifeapps.ui.detailorder.diproses.DiprosesFragment
import com.ibunda.mitrailifeapps.ui.detailorder.pesanan.PesananFragment
import com.ibunda.mitrailifeapps.ui.detailorder.selesai.SelesaiFragment
import com.ibunda.mitrailifeapps.utils.AppConstants.STATUS_DIBATALKAN
import com.ibunda.mitrailifeapps.utils.AppConstants.STATUS_DIPROSES
import com.ibunda.mitrailifeapps.utils.AppConstants.STATUS_PESANAN
import com.ibunda.mitrailifeapps.utils.AppConstants.STATUS_SELESAI
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel: DetailViewModel by viewModels()

    private lateinit var ordersData: Orders
    private lateinit var shopsDataProfile: Shops

    companion object {
        const val PESANAN_FRAGMENT_TAG = "pesanan_fragment_tag"
        const val DIPROSES_FRAGMENT_TAG = "diproses_fragment_tag"
        const val SELESAI_FRAGMENT_TAG = "selesai_fragment_tag"
        const val DIBATALKAN_FRAGMENT_TAG = "dibatalkan_fragment_tag"
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ORDER = "extra_order"
        const val EXTRA_ORDER_ID = "extra_order_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(EXTRA_ORDER)) {
            ordersData = intent.getParcelableExtra<Orders>(EXTRA_ORDER) as Orders
            setOrderData(ordersData.orderId)
        } else if (intent.hasExtra(EXTRA_ORDER_ID)) {
            setOrderData(intent.getStringExtra(EXTRA_ORDER_ID))
        }

    }

    private fun setOrderData(orderId: String?) {
        detailViewModel.setOrderData(orderId.toString()).observe(this, { orders ->
            if (orders != null) {
                ordersData = orders
                setShop(ordersData)
                setOrderCondition(ordersData.status)
                Log.e(ordersData.status, "statusOrder")
            }
        })
    }

    private fun setCurrentFragment(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.commit {
            replace(R.id.host_detail_activity, fragment, fragmentTag)
        }
    }

    private fun setShop(ordersData: Orders) {
        detailViewModel.setShopsProfile(ordersData.shopId.toString())
            .observe(this, { shopsProfile ->
                if (shopsProfile != null) {
                    shopsDataProfile = shopsProfile
                }
            })
    }

    private fun setOrderCondition(status: String?) {

        val pesananFragment = PesananFragment()
        val diprosesFragment = DiprosesFragment()
        val selesaiFragment = SelesaiFragment()
        val dibatalkanFragment = DibatalkanFragment()

        when (status) {
            STATUS_PESANAN -> {
                setCurrentFragment(pesananFragment, PESANAN_FRAGMENT_TAG)
            }
            STATUS_DIPROSES -> {
                setCurrentFragment(diprosesFragment, DIPROSES_FRAGMENT_TAG)
            }
            STATUS_SELESAI -> {
                setCurrentFragment(selesaiFragment, SELESAI_FRAGMENT_TAG)
            }
            STATUS_DIBATALKAN -> {
                setCurrentFragment(dibatalkanFragment, DIBATALKAN_FRAGMENT_TAG)
            }
        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            supportFragmentManager.popBackStack()
        }
    }

}