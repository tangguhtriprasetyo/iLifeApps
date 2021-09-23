package com.ibunda.mitrailifeapps.ui.dashboard.transaction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayoutMediator
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.FragmentTransactionBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainActivity
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.ui.dashboard.chats.ChatsActivity
import com.ibunda.mitrailifeapps.ui.dashboard.notifications.NotificationsFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop.CreateShopOneFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TransactionFragment : Fragment() {

    private lateinit var binding : FragmentTransactionBinding

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var mitraDataProfile: Mitras
    private lateinit var shopsDataProfile: Shops

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
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
                        initTabLayout()
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
            setCurrentFragment(mCreateShopOneFragment)
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

    private fun initTabLayout() {

        mainViewModel.getShopData()
            .observe(viewLifecycleOwner, { shopsProfile ->
                if (shopsProfile != null) {
                    shopsDataProfile = shopsProfile
                    listenNotif(shopsDataProfile.shopId)
                    listenChats(shopsDataProfile.shopId)
                }
                Log.d("ViewModelShopsProfile: ", shopsProfile.toString())
            })

        binding.notification.icNotification.setOnClickListener { gotoNotifications() }
        binding.chat.icMessage.setOnClickListener { gotoChats() }

        binding.tabLayout.visibility = View.VISIBLE
        binding.viewPager.visibility = View.VISIBLE

        val sectionAdapter = SectionTransactionAdapter(this)
        binding.viewPager.adapter = sectionAdapter
        binding.viewPager.isSaveEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Diproses"
                1 -> tab.text = "Selesai"
                2 -> tab.text = "Dibatalkan"
            }
        }.attach()
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