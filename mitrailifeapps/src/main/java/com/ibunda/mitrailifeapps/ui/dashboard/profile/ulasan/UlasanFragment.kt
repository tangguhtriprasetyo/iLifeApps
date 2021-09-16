package com.ibunda.mitrailifeapps.ui.dashboard.profile.ulasan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.FragmentUlasanBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UlasanFragment : Fragment() {

    private lateinit var binding: FragmentUlasanBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var shopsDataProfile: Shops

    private val ulasanAdapter = UlasanAdapter()

    companion object {
        const val FROM_TRANSACTION = "from_transaction"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUlasanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initShops()

        val bottomNav: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE

        val ulasan = requireArguments().getBoolean(FROM_TRANSACTION, false)
        Log.e(ulasan.toString(), "ulasanInfo")
        binding.icBack.setOnClickListener {
            if (ulasan) {
                activity?.onBackPressed()
            } else {
                bottomNav.visibility = View.VISIBLE
                requireActivity().supportFragmentManager.popBackStackImmediate()
            }
        }

    }

    private fun initShops() {
        mainViewModel.getShopData()
            .observe(viewLifecycleOwner, { shopsProfile ->
                if (shopsProfile != null) {
                    shopsDataProfile = shopsProfile
                    setDataShops(shopsDataProfile)
                    setDataRvListUlasan(shopsDataProfile)
                }
                Log.d("ViewModelShopsProfile: ", shopsProfile.toString())
            })
    }

    private fun setDataShops(shopsDataProfile: Shops) {
        with(binding) {
            tvValueRating.text = shopsDataProfile.rating.toString()
            ratingBar.rating = (shopsDataProfile.rating?.toFloat()!!)
            tvDescRating.text =
                shopsDataProfile.rating.toString() + " dari 5 " + "(" + shopsDataProfile.totalUlasan + " ulasan)"
        }
    }

    private fun setDataRvListUlasan(shopsDataProfile: Shops) {
        showProgressBar(true)
        mainViewModel.getListUlasan(shopsDataProfile.shopId.toString())
            .observe(viewLifecycleOwner, { listUlasan ->
                if (listUlasan != null && listUlasan.isNotEmpty()) {
                    showEmptyOrder(false)
                    showProgressBar(false)
                    ulasanAdapter.setListUlasan(listUlasan)
                    setOrderAdapter()
                } else {
                    showEmptyOrder(true)
                    showProgressBar(false)
                }
            })
    }

    private fun setOrderAdapter() {
        with(binding.rvUlasan) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = ulasanAdapter
        }
    }

    private fun showEmptyOrder(state: Boolean) {
        if (state) {
            binding.linearEmptyPilihMitra.visibility = View.VISIBLE
            binding.rvUlasan.visibility = View.GONE
        } else {
            binding.rvUlasan.visibility = View.VISIBLE
            binding.linearEmptyPilihMitra.visibility = View.GONE
        }
    }

    private fun showProgressBar(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


}