package com.ibunda.mitrailifeapps.ui.dashboard.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.databinding.FragmentProfileBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainActivity
import com.ibunda.mitrailifeapps.ui.dashboard.profile.setting.SettingFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.ulasan.UlasanFragment


class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentProfileBinding

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

        initView()

    }

    private fun initView() {
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

}