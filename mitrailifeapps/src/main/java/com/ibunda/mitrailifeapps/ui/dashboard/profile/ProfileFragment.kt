package com.ibunda.mitrailifeapps.ui.dashboard.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.databinding.FragmentProfileBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainActivity
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop.CreateShopOneFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.setting.SettingFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.ulasan.UlasanFragment


class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentProfileBinding

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var mitraDataProfile: Mitras

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
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

    private fun initEmptyShop() {
        binding.linearEmptyToko.visibility = View.VISIBLE
        binding.linearMessage.visibility = View.GONE
        binding.linearNotification.visibility = View.GONE
        binding.icSetting.visibility = View.GONE

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

    private fun initView() {

        binding.linearToko.visibility = View.VISIBLE

        binding.linearMessage.setOnClickListener(this)
        binding.linearNotification.setOnClickListener(this)
        binding.icSetting.setOnClickListener(this)
        binding.linearDeskripsiToko.setOnClickListener(this)
        binding.linearEditAkun.setOnClickListener(this)
        binding.linearUlasanToko.setOnClickListener(this)
        binding.btnKelolaToko.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.linear_message -> openMessage()
            R.id.linear_notification -> openNotifications()
            R.id.ic_setting -> openSetting()
            R.id.linear_deskripsi_toko -> editAkun("EditKemampuan")
            R.id.linear_edit_akun -> editAkun("EditShop")
            R.id.linear_ulasan_toko -> openUlasan()
            R.id.btn_kelola_toko -> kelolaToko()
        }
    }

    private fun kelolaToko() {

    }

    private fun openUlasan() {
        val mFragmentManager = parentFragmentManager
        val mUlasanFragment = UlasanFragment()
        mFragmentManager.commit {
            addToBackStack(null)
            replace(
                R.id.host_fragment_activity_main,
                mUlasanFragment,
                MainActivity.CHILD_FRAGMENT
            )
        }
    }

    private fun editAkun(value: String) {

    }

    private fun openSetting() {
        val mSettingFragment = SettingFragment()
        val mFragmentManager = parentFragmentManager
        mFragmentManager.commit {
            addToBackStack(null)
            replace(
                R.id.host_fragment_activity_main,
                mSettingFragment,
                MainActivity.CHILD_FRAGMENT
            )
        }
    }

    private fun openNotifications() {

    }

    private fun openMessage() {
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