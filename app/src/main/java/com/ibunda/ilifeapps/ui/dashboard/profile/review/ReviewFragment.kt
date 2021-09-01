package com.ibunda.ilifeapps.ui.dashboard.profile.review

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentReviewBinding
import com.ibunda.ilifeapps.ui.dashboard.MainViewModel


class ReviewFragment : Fragment() {

    private lateinit var binding : FragmentReviewBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var userDataProfile: Users
    private val reviewAdapter = ReviewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReviewBinding.inflate(inflater, container, false)
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

        getUserData()
    }

    private fun getUserData() {
        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                    setDataRvUlasan(userDataProfile.userId)
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })
    }

    private fun setDataRvUlasan(userId: String?) {
        mainViewModel.getListUlasan(userId.toString()).observe(viewLifecycleOwner, { listUlasan ->
            if (listUlasan != null && listUlasan.isNotEmpty()) {
                reviewAdapter.setListUlasan(listUlasan)
                showEmptyUlasan(false)
                setUlasanAdapter()
            } else {
                showEmptyUlasan(true)
            }
        })
    }

    private fun setUlasanAdapter() {
        with(binding.rvPenilaian) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = reviewAdapter
            android.util.Log.d(android.content.ContentValues.TAG, "setAdapter: ")
        }
    }

    private fun showEmptyUlasan(state: Boolean) {
        if (state) {
            binding.rvPenilaian.visibility = View.GONE
            binding.linearEmptyPenilaian.visibility = View.VISIBLE
        } else {
            binding.rvPenilaian.visibility = View.VISIBLE
        }
    }

}