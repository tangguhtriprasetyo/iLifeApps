package com.ibunda.ilifeapps.ui.dashboard.home.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentNotificationsBinding
import com.ibunda.ilifeapps.ui.dashboard.MainViewModel

class NotificationsFragment : Fragment() {
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var userDataProfile: Users

    private val mainViewModel: MainViewModel by activityViewModels()
    private val notifAdapter = NotificationsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initData() {
        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                    setDataRvNotif()
                }
            })
    }

    private fun setDataRvNotif() {
        mainViewModel.getListNotif(userDataProfile.userId.toString())
            .observe(viewLifecycleOwner, { dataNotif ->
                if (dataNotif != null && dataNotif.isNotEmpty()) {
                    notifAdapter.setListNotif(dataNotif)
                    setNotifAdapter()
                    showEmptyNotif(false)
                } else {
                    showEmptyNotif(true)
                }
            })
    }

    private fun showEmptyNotif(state: Boolean) {
        if (state) {
            binding.rvNotification.visibility = View.GONE
            binding.linearEmptyNotifikasi.visibility = View.VISIBLE
        } else {
            binding.linearEmptyNotifikasi.visibility = View.GONE
            binding.rvNotification.visibility = View.VISIBLE
        }
    }

    private fun setNotifAdapter() {
        with(binding.rvNotification) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = notifAdapter
        }
    }

    private fun initView() {
        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}