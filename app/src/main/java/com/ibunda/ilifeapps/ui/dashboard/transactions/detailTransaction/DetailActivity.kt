package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.databinding.ActivityDetailBinding
import com.ibunda.ilifeapps.ui.dashboard.transactions.TransactionViewModel
import com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.dibatalkan.DibatalkanFragment
import com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.diproses.DiprosesFragment
import com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.PesananFragment
import com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.selesai.SelesaiFragment
import com.ibunda.ilifeapps.utils.AppConstants

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val transactionViewModel: TransactionViewModel by viewModels()
    private lateinit var ordersData: Orders

    companion object {
        const val PESANAN_FRAGMENT_TAG = "pesanan_fragment_tag"
        const val DIPROSES_FRAGMENT_TAG = "diproses_fragment_tag"
        const val SELESAI_FRAGMENT_TAG = "selesai_fragment_tag"
        const val DIBATALKAN_FRAGMENT_TAG = "dibatalkan_fragment_tag"
        const val EXTRA_ORDER = "extra_order"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordersData = intent.getParcelableExtra<Orders>(EXTRA_ORDER) as Orders


        setOrderData()
        setOrderCondition(ordersData.status)

    }

    private fun setOrderCondition(status: String?) {

        val pesananFragment = PesananFragment()
        val diprosesFragment = DiprosesFragment()
        val selesaiFragment = SelesaiFragment()
        val dibatalkanFragment = DibatalkanFragment()

        when(status) {
            AppConstants.STATUS_PESANAN -> {
                setCurrentFragment(pesananFragment, PESANAN_FRAGMENT_TAG)
            }
            AppConstants.STATUS_DIPROSES -> {
                setCurrentFragment(diprosesFragment, DIPROSES_FRAGMENT_TAG)
            }
            AppConstants.STATUS_SELESAI -> {
                setCurrentFragment(selesaiFragment, SELESAI_FRAGMENT_TAG)
            }
            AppConstants.STATUS_DIBATALKAN -> {
                setCurrentFragment(dibatalkanFragment, DIBATALKAN_FRAGMENT_TAG)
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.commit {
            replace(R.id.host_detail_activity, fragment, fragmentTag)
        }
    }

    private fun setOrderData() {
        transactionViewModel.setOrderData(ordersData.orderId.toString()).observe(this, { orders ->
            if (orders != null) {
                ordersData = orders
                Log.e(ordersData.status, "statusOrder")
            }
        })
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