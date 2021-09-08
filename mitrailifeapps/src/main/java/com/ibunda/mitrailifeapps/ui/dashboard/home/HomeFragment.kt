package com.ibunda.mitrailifeapps.ui.dashboard.home

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
import com.ibunda.mitrailifeapps.databinding.FragmentHomeBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainActivity
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.ui.dashboard.home.dialogseleksiberdasarkan.DialogSeleksiBerdasarkanFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop.CreateShopOneFragment
import com.ibunda.mitrailifeapps.utils.AppConstants.STATUS_PESANAN
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private val homeAdapter = HomeAdapter()

    private lateinit var mitraDataProfile: Mitras

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
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
                        initView()
                    }
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })

    }

    private fun initView() {
        binding.rvPesananKhusus.visibility = View.VISIBLE

        setDataRvListOrders()
        binding.linearSortby.setOnClickListener(this)
        binding.linearMessage.setOnClickListener(this)
        binding.linearNotification.setOnClickListener(this)
    }

    private fun initEmptyShop() {
        binding.linearEmptyToko.visibility = View.VISIBLE
        binding.linearMessage.visibility = View.GONE
        binding.linearNotification.visibility = View.GONE
        binding.linearSortby.visibility = View.GONE

        binding.btnTambahToko.setOnClickListener {
            val mCreateShopOneFragment = CreateShopOneFragment()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.commit {
                addToBackStack(null)
                replace(
                    R.id.host_fragment_activity_main,
                    mCreateShopOneFragment,
                    MainActivity.CHILD_FRAGMENT
                )
            }
        }
    }

    private fun setDataRvListOrders() {
        mainViewModel.getListOrderKhusus(true, STATUS_PESANAN).observe(viewLifecycleOwner, { listOrders ->
            if (listOrders != null) {
                homeAdapter.setListOrders(listOrders)
                setOrdersAdapter()
            }
        })
    }

    private fun setOrdersAdapter() {
        with(binding.rvPesananKhusus) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = homeAdapter
            android.util.Log.d(android.content.ContentValues.TAG, "setAdapter: ")
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ic_message -> openMessage()
            R.id.ic_notification -> openNotifications()
            R.id.linear_sortby -> dialogSeleksiBerdasarkan()
        }
    }

    private fun openMessage() {

    }

    private fun openNotifications() {

    }

    private fun dialogSeleksiBerdasarkan() {
        val mDialogSeleksiBerdasarkanFragment = DialogSeleksiBerdasarkanFragment()
        val mFragmentManager = childFragmentManager
        mDialogSeleksiBerdasarkanFragment.show(
            mFragmentManager,
            mDialogSeleksiBerdasarkanFragment::class.java.simpleName
        )
    }

    internal var optionDialogListener: DialogSeleksiBerdasarkanFragment.OnOptionDialogListener =
        object : DialogSeleksiBerdasarkanFragment.OnOptionDialogListener {
            override fun onOptionChosen(category: String?) {
                val kategoriMitra: String? = category

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
                        initView()
                    }
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })
    }

}