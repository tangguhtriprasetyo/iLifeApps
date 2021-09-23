package com.ibunda.mitrailifeapps.ui.dashboard.notifications

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Notifications
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.FragmentNotificationsBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.ui.detailorder.DetailActivity
import com.ibunda.mitrailifeapps.utils.AppConstants
import com.ibunda.mitrailifeapps.utils.ProgressDialogHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class NotificationsFragment : Fragment(), NotificationsClickCallback {
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var shopsDataProfile: Shops

    private val mainViewModel: MainViewModel by activityViewModels()
    private val notifAdapter = NotificationsAdapter(this@NotificationsFragment)

    private lateinit var progressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialogHelper.progressDialog(requireContext())
        val bottomNav: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE

        initView()
        initData()
    }

    private fun initData() {
        mainViewModel.getShopData()
            .observe(viewLifecycleOwner, { shopsProfile ->
                if (shopsProfile != null) {
                    shopsDataProfile = shopsProfile
                    setDataRvNotif()
                }
            })
    }

    private fun setDataRvNotif() {
        showProgressBar(true)
        mainViewModel.getListNotif(shopsDataProfile.shopId.toString())
            .observe(viewLifecycleOwner, { dataNotif ->
                if (dataNotif != null && dataNotif.isNotEmpty()) {
                    showProgressBar(false)
                    notifAdapter.setListNotif(dataNotif)
                    setNotifAdapter()
                    showEmptyNotif(false)
                } else {
                    showProgressBar(false)
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

    private fun showProgressBar(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
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

    override fun onItemClicked(data: Notifications) {
        progressDialog.show()
        mainViewModel.deleteNotif(data.notifId.toString())
            .observe(viewLifecycleOwner, { status ->
                if (status == AppConstants.STATUS_SUCCESS) {
                    progressDialog.dismiss()
                    val intent =
                        Intent(requireActivity(), DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_ORDER_ID, data.orderId)
                    requireActivity().startActivity(intent)
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                }
            })
    }
}