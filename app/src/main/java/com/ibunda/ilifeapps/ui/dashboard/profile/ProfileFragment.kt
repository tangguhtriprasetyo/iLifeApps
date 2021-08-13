package com.ibunda.ilifeapps.ui.dashboard.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentProfileBinding
import com.ibunda.ilifeapps.ui.dashboard.MainActivity
import com.ibunda.ilifeapps.ui.dashboard.MainViewModel
import com.ibunda.ilifeapps.utils.loadImage


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var userDataProfile: Users

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

        getUserData()
        initView()

    }

    private fun getUserData() {
        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                    setProfileData(userDataProfile)
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })
    }

    private fun setProfileData(userDataProfile: Users) {
        with(binding) {
            imgProfile.loadImage(userDataProfile.avatar)
            tvName.text = userDataProfile.name
            tvEmail.text = userDataProfile.email
            tvPhone.text = userDataProfile.phone
            tvGender.text = userDataProfile.gender
            tvTtl.text = userDataProfile.ttl
            tvAddress.text = userDataProfile.address
        }
    }

    private fun userLogout() {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val googleSignInClient: GoogleSignInClient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        firebaseAuth.signOut()
        googleSignInClient.signOut()
    }

    private fun initView() {

        binding.linearEditAkun.setOnClickListener {
            val mFragmentManager = parentFragmentManager
            val mEditProfileFragment = EditProfileFragment()
            mFragmentManager.commit {
                addToBackStack(null)
                replace(
                    R.id.host_fragment_activity_main,
                    mEditProfileFragment,
                    MainActivity.CHILD_FRAGMENT
                )
            }
        }

        binding.linearLogout.setOnClickListener {
            userLogout()
        }
    }
}