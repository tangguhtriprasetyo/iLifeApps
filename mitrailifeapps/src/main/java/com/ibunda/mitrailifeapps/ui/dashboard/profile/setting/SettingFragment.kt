package com.ibunda.mitrailifeapps.ui.dashboard.profile.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.databinding.FragmentSettingBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainActivity
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.ui.dashboard.profile.setting.editakunmitra.EditAkunMitraFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class SettingFragment : Fragment() {

    private lateinit var binding : FragmentSettingBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var mitraDataProfile: Mitras

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE
        binding.icBack.setOnClickListener {
            bottomNav.visibility = View.VISIBLE
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

        getMitrasData()
        initOnClick()

    }

    private fun initOnClick() {
        binding.btnEditAkun.setOnClickListener {
            editAkun("EditAkun")
        }
        binding.btnChangePassword.setOnClickListener {
            editAkun("EditPassword")
        }

        binding.btnLogout.setOnClickListener {
            userLogout()
        }
    }

    private fun userLogout() {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
    }

    private fun editAkun(value: String) {
        val mEditAkunMitraFragment = EditAkunMitraFragment()
        val mBundle = Bundle()
        mBundle.putString(EditAkunMitraFragment.EXTRA_NAME, value)
        mEditAkunMitraFragment.arguments = mBundle

        val mFragmentManager = parentFragmentManager
        mFragmentManager.commit {
            addToBackStack(null)
            replace(
                R.id.host_fragment_activity_main,
                mEditAkunMitraFragment,
                MainActivity.CHILD_FRAGMENT
            )
        }
    }

    private fun getMitrasData() {
        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    mitraDataProfile = userProfile
                    setProfileData(mitraDataProfile)
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })
    }

    private fun setProfileData(mitraDataProfile: Mitras) {
        with(binding) {
            tvBergabungSejak.text = mitraDataProfile.registeredAt
            tvUsername.text = mitraDataProfile.name
            tvEmail.text = mitraDataProfile.email
            tvAddress.text = mitraDataProfile.address
        }
    }

}