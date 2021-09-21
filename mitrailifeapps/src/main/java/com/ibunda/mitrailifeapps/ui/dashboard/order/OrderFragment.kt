package com.ibunda.mitrailifeapps.ui.dashboard.order

import android.content.Intent
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
import com.ibunda.mitrailifeapps.ui.dashboard.MainActivity
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.ui.dashboard.chats.ChatsActivity
import com.ibunda.mitrailifeapps.ui.dashboard.notifications.NotificationsFragment
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

        getProfileData()

    }

    private fun getProfileData() {
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
        binding.chat.icMessage.visibility = View.GONE
        binding.chat.imgBadgeChat.visibility = View.GONE
        binding.notification.icNotification.visibility = View.GONE
        binding.notification.imgBadgeNotification.visibility = View.GONE

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

    private fun initShops() {
        mainViewModel.getShopData()
            .observe(viewLifecycleOwner, { shopsProfile ->
                if (shopsProfile != null) {
                    shopsDataProfile = shopsProfile
                    setDataRvListOrders(shopsDataProfile)
                    listenNotif(shopsDataProfile.shopId)
                    listenChats(shopsDataProfile.shopId)
                }
                Log.d("ViewModelShopsProfile: ", shopsProfile.toString())
            })
        binding.rvPesanan.visibility = View.VISIBLE

        binding.notification.icNotification.setOnClickListener { gotoNotifications() }
        binding.chat.icMessage.setOnClickListener { gotoChats() }
    }

    private fun gotoNotifications() {
        val mFragmentManager = parentFragmentManager
        val mCustomOrderFragment = NotificationsFragment()
        mFragmentManager.commit {
            addToBackStack(null)
            replace(
                R.id.host_fragment_activity_main,
                mCustomOrderFragment,
                MainActivity.CHILD_FRAGMENT
            )
        }
    }

    private fun gotoChats() {
        val intent = Intent(requireActivity(), ChatsActivity::class.java)
        intent.putExtra(ChatsActivity.EXTRA_USER, shopsDataProfile)
        startActivity(intent)
    }

    private fun listenChats(shopId: String?) {
        mainViewModel.getListChatRoom(shopId.toString())
            .observe(viewLifecycleOwner, { listChats ->
                if (listChats != null && listChats.isNotEmpty()) {
                    binding.chat.imgBadgeChat.visibility = View.VISIBLE
                } else {
                    binding.chat.imgBadgeChat.visibility = View.GONE
                }
            })
    }

    private fun listenNotif(shopId: String?) {
        mainViewModel.getListNotif(shopId.toString())
            .observe(viewLifecycleOwner, { listNotif ->
                if (listNotif != null && listNotif.isNotEmpty()) {
                    binding.notification.imgBadgeNotification.visibility = View.VISIBLE
                } else {
                    binding.notification.imgBadgeNotification.visibility = View.GONE
                }
            })
    }

    private fun setDataRvListOrders(shopsDataProfile: Shops) {
        showProgressBar(true)
        mainViewModel.getListOrders(STATUS_PESANAN, shopsDataProfile.shopId.toString()).observe(viewLifecycleOwner, { listOrders ->
            if (listOrders != null && listOrders.isNotEmpty()) {
                showProgressBar(false)
                showEmptyOrder(false)
                orderAdapter.setListOrders(listOrders)
                setOrderAdapter()
            } else {
                showProgressBar(false)
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
            binding.rvPesanan.visibility = View.GONE
        } else {
            binding.linearEmptyPesanan.visibility = View.GONE
            binding.rvPesanan.visibility = View.VISIBLE
        }
    }

    private fun showProgressBar(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        getProfileData()
    }

}