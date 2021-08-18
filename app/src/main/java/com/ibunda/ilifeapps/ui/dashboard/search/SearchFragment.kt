package com.ibunda.ilifeapps.ui.dashboard.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentSearchBinding
import com.ibunda.ilifeapps.ui.dashboard.MainViewModel
import com.ibunda.ilifeapps.ui.dashboard.search.adsSearch.AdsSearchAdapter
import com.ibunda.ilifeapps.ui.dashboard.search.otherlistshop.OtherListShopAdapter
import com.ibunda.ilifeapps.ui.dashboard.search.promolistshop.PromoListShopAdapter


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var userDataProfile: Users
    private val otherListShopAdapter = OtherListShopAdapter()
    private val promoListShopAdapter = PromoListShopAdapter()
    private val adsSearchAdapter = AdsSearchAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {

                    userDataProfile = userProfile
                    if (userDataProfile.address != null) {
                        binding.tvAddress.text = userDataProfile.address
                    } else {
                        binding.tvAddress.text = "Tentukan Lokasi Anda"
                    }
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })

        setRvSearch()
        setDataRvListOtherShop()
        setDataRvListPromoShop()
    }

    private fun setRvSearch() {
        mainViewModel.getListAds("adsSearch").observe(viewLifecycleOwner, { listAds ->
            if (listAds != null) {
                adsSearchAdapter.setListAds(listAds)
                setAdsAdapter()
            }
        })
    }

    private fun setAdsAdapter() {
        with(binding.rvAdsSearch) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = adsSearchAdapter
            android.util.Log.d(android.content.ContentValues.TAG, "setAdapter: ")
        }
    }

    private fun setDataRvListPromoShop() {
        mainViewModel.getListPromoShop(true).observe(viewLifecycleOwner, { listShops ->
            if (listShops != null) {
                promoListShopAdapter.setListShops(listShops)
                setPromoShopsAdapter()
            }
        })
    }

    private fun setPromoShopsAdapter() {
        with(binding.rvSedangDiskon) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = promoListShopAdapter
            android.util.Log.d(android.content.ContentValues.TAG, "setAdapter: ")
        }
    }

    //otherShops
    private fun setDataRvListOtherShop() {
        mainViewModel.getListOtherShop("Lainnya").observe(viewLifecycleOwner, { listShops ->
            if (listShops != null) {
                otherListShopAdapter.setListShops(listShops)
                setOtherShopsAdapter()
            }
        })
    }

    private fun setOtherShopsAdapter() {
        with(binding.rvMitraLainnya) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = otherListShopAdapter
            android.util.Log.d(android.content.ContentValues.TAG, "setAdapter: ")
        }
    }
}
