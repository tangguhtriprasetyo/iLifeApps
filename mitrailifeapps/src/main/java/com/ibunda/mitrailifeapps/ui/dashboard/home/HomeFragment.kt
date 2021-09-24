package com.ibunda.mitrailifeapps.ui.dashboard.home

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
import com.ibunda.mitrailifeapps.databinding.FragmentHomeBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainActivity
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.ui.dashboard.chats.ChatsActivity
import com.ibunda.mitrailifeapps.ui.dashboard.home.dialogseleksiberdasarkan.DialogSeleksiBerdasarkanFragment
import com.ibunda.mitrailifeapps.ui.dashboard.notifications.NotificationsFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop.CreateShopOneFragment
import com.ibunda.mitrailifeapps.utils.AppConstants.STATUS_PESANAN
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val homeAdapter = HomeAdapter()
    private var categoryOrder: String? = null
    private var sort: Boolean = false
    private lateinit var mitraDataProfile: Mitras
    private lateinit var shopsDataProfile: Shops

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
                        initView()
                    }
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })
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

    private fun initView() {
        mainViewModel.getShopData()
            .observe(viewLifecycleOwner, { shopsProfile ->
                if (shopsProfile != null) {
                    shopsDataProfile = shopsProfile
                    listenNotif(shopsDataProfile.shopId)
                    listenChats(shopsDataProfile.shopId)
                }
                Log.d("ViewModelShopsProfile: ", shopsProfile.toString())
            })

        setDataRvListOrders(sort)
        binding.linearSortby.setOnClickListener(this)
        binding.notification.icNotification.setOnClickListener(this)
        binding.chat.icMessage.setOnClickListener(this)
    }

    private fun initEmptyShop() {
        binding.linearEmptyToko.visibility = View.VISIBLE
        binding.chat.icMessage.visibility = View.GONE
        binding.chat.imgBadgeChat.visibility = View.GONE
        binding.notification.icNotification.visibility = View.GONE
        binding.notification.imgBadgeNotification.visibility = View.GONE
        binding.linearSortby.visibility = View.GONE

        binding.btnTambahToko.setOnClickListener {
            val mCreateShopOneFragment = CreateShopOneFragment()
            setCurrentFragment(mCreateShopOneFragment)
        }
    }

    private fun setDataRvListOrders(sort : Boolean) {
        showProgressBar(true)
        mainViewModel.getListOrderKhusus(true, STATUS_PESANAN,sort, categoryOrder.toString()).observe(viewLifecycleOwner, { listOrders ->
            if (listOrders != null && listOrders.isNotEmpty()) {
                showProgressBar(false)
                showEmptyOrder(false)
                homeAdapter.setListOrders(listOrders)
                setOrdersAdapter()
            } else {
                showProgressBar(false)
                showEmptyOrder(true)
            }
        })
    }

    private fun setOrdersAdapter() {
        with(binding.rvPesananKhusus) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = homeAdapter
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ic_message -> gotoChats()
            R.id.ic_notification -> gotoNotifications()
            R.id.linear_sortby -> dialogSeleksiBerdasarkan()
        }
    }

    private fun gotoNotifications() {
        val mNotificationsFragment = NotificationsFragment()
        setCurrentFragment(mNotificationsFragment)
    }

    private fun gotoChats() {
        val intent = Intent(requireActivity(), ChatsActivity::class.java)
        intent.putExtra(ChatsActivity.EXTRA_USER, shopsDataProfile)
        startActivity(intent)
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
                categoryOrder = category
                sort = categoryOrder != "Semua"
                setDataRvListOrders(sort)
            }
        }

    private fun showEmptyOrder(state: Boolean) {
        if (state) {
            binding.linearEmptyPesanan.visibility = View.VISIBLE
            binding.rvPesananKhusus.visibility = View.GONE
        } else {
            binding.rvPesananKhusus.visibility = View.VISIBLE
            binding.linearEmptyPesanan.visibility = View.GONE
        }
    }

    private fun showProgressBar(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        parentFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.host_fragment_activity_main, fragment, MainActivity.CHILD_FRAGMENT)
        }
    }

    override fun onResume() {
        super.onResume()
        getProfileData()
    }


}