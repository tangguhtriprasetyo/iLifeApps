package com.ibunda.ilifeapps.ui.dashboard.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentSearchBinding
import com.ibunda.ilifeapps.ui.dashboard.MainViewModel
import com.ibunda.ilifeapps.ui.dashboard.search.adsSearch.AdsSearchAdapter
import com.ibunda.ilifeapps.ui.dashboard.search.otherlistshop.OtherListShopAdapter
import com.ibunda.ilifeapps.ui.dashboard.search.promolistshop.PromoListShopAdapter
import com.ibunda.ilifeapps.ui.listmitra.ListMitraActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
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
    ): View {
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
                        binding.tvAddress.text = getString(R.string.tv_tentukan_lokasi)
                    }
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })

        setRvSearch()
        setDataRvListOtherShop()
        setDataRvListPromoShop()
        initOnClick()
    }

    private fun initOnClick() {
        binding.tvLainnyaSemua.setOnClickListener {
            gotoListShop("Lainnya", userDataProfile, false, null)
        }
        binding.tvDiskonSemua.setOnClickListener {
            gotoListShop("Sedang Diskon", userDataProfile, true, null)
        }

        binding.etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = binding.etSearch.text.toString()
                gotoListShop("Hasil Pencarian", userDataProfile, false, searchQuery)
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun gotoListShop(
        categoryName: String?,
        userDataProfile: Users,
        promo: Boolean,
        search: String?
    ) {
        val intent =
            Intent(requireActivity(), ListMitraActivity::class.java)
        intent.putExtra(ListMitraActivity.EXTRA_CATEGORY_NAME, categoryName)
        intent.putExtra(ListMitraActivity.EXTRA_USER, userDataProfile.userId)
        intent.putExtra(ListMitraActivity.EXTRA_PROMO, promo)
        intent.putExtra(ListMitraActivity.EXTRA_SEARCH, search)
        startActivity(intent)
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
            Log.d(android.content.ContentValues.TAG, "setAdapter: ")
        }
    }

    private fun setDataRvListPromoShop() {
        mainViewModel.getListPromoShop(true).observe(viewLifecycleOwner, { listShops ->
            if (listShops != null) {
                promoListShopAdapter.setListShops(listShops, userDataProfile)
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
            Log.d(android.content.ContentValues.TAG, "setAdapter: ")
        }
    }

    //otherShops
    private fun setDataRvListOtherShop() {
        mainViewModel.getListOtherShop("Lainnya").observe(viewLifecycleOwner, { listShops ->
            if (listShops != null) {
                otherListShopAdapter.setListShops(listShops, userDataProfile)
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
            Log.d(android.content.ContentValues.TAG, "setAdapter: ")
        }
    }
}
