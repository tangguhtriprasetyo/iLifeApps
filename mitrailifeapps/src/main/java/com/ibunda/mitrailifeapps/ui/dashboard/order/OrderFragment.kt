package com.ibunda.mitrailifeapps.ui.dashboard.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.FragmentOrderBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop.CreateShopOneFragment
import com.ibunda.mitrailifeapps.utils.AppConstants.STATUS_PESANAN
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class OrderFragment : Fragment() {

    private lateinit var binding : FragmentOrderBinding

    private val mainViewModel: MainViewModel by activityViewModels()

    private val orderAdapter = OrderAdapter()

    private lateinit var mitraDataProfile: Mitras
    private lateinit var shopsDataProfile: Shops


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    mitraDataProfile = userProfile
                    if (mitraDataProfile.totalShop == 0) {
                        initEmptyShop()
                    } else {
                        initShops()
                    }
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })

    }

    private fun initEmptyShop() {
        binding.linearEmptyToko.visibility = View.VISIBLE
        binding.linearMessage.visibility = View.GONE
        binding.linearNotification.visibility = View.GONE

        binding.btnTambahToko.setOnClickListener {
            val mCreateShopOneFragment = CreateShopOneFragment()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.commit {
                addToBackStack(null)
                replace(
                    R.id.host_fragment_activity_main,
                    mCreateShopOneFragment
                )
            }
        }
    }

    private fun initShops() {
        mainViewModel.getShopData()
            .observe(viewLifecycleOwner, { shopsProfile ->
                if (shopsProfile != null) {
                    shopsDataProfile = shopsProfile
                    setDataRvListOrders(shopsDataProfile)
                }
                Log.d("ViewModelShopsProfile: ", shopsProfile.toString())
            })

        binding.rvPesanan.visibility = View.VISIBLE
    }

    private fun setDataRvListOrders(shopsDataProfile: Shops) {
        mainViewModel.getListOrders(STATUS_PESANAN, shopsDataProfile.shopId.toString()).observe(viewLifecycleOwner, { listOrders ->
            if (listOrders != null && listOrders.isNotEmpty()) {
                showEmptyOrder(false)
                orderAdapter.setListOrders(listOrders)
                setOrderAdapter()
            } else {
                showEmptyOrder(true)
            }
        })
    }

    private fun setOrderAdapter() {
        with(binding.rvPesanan) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = orderAdapter
        }
    }

    private fun showEmptyOrder(state: Boolean) {
        if (state) {
            binding.linearEmptyPesanan.visibility = View.VISIBLE
        } else {
            binding.rvPesanan.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    mitraDataProfile = userProfile
                    if (mitraDataProfile.totalShop == 0) {
                        initEmptyShop()
                    } else {
                        initShops()
                    }
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })
    }

}