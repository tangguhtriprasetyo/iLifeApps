package com.ibunda.ilifeapps.ui.listmitra.listshop

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentListShopBinding
import com.ibunda.ilifeapps.ui.listmitra.ListMitraViewModel


class ListShopFragment : Fragment() {

    private lateinit var binding: FragmentListShopBinding
    private lateinit var user: Users

    private val listShopViewModel: ListShopViewModel by activityViewModels()
    private val listMitraViewModel: ListMitraViewModel by activityViewModels()
    private val listShopAdapter = ListShopAdapter()

    companion object {
        const val EXTRA_PROMO = "extra_promo"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            setDataUser()
        }

        initView()

    }

    private fun initView() {
        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setDataUser() {
        listMitraViewModel.getProfileData().observe(viewLifecycleOwner, { userData ->
            if (userData != null) {
                user = userData

                if (requireArguments().getBoolean(EXTRA_PROMO)) {
                    binding.tvListKategoriMitra.text = "Sedang Diskon"
                    setDataRvListPromoShop()
                } else {
                    listMitraViewModel.dataCategory?.observe(viewLifecycleOwner, Observer {
                        binding.tvListKategoriMitra.text = listMitraViewModel.dataCategory.value
                    })
                    setDataRvListShop()
                }

            }
        })
    }

    private fun setShopsAdapter() {
        with(binding.rvListMitra) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = listShopAdapter
            Log.d(TAG, "setAdapter: ")
        }
    }

    private fun setDataRvListShop() {
        listMitraViewModel.dataCategory.value?.let {
            listShopViewModel.getListShop(it).observe(viewLifecycleOwner, { listShops ->
                if (listShops != null && listShops.isNotEmpty()) {
                    listShopAdapter.setListShops(listShops, user)
                    setShopsAdapter()
                    showEmptyListShop(false)
                } else {
                    showEmptyListShop(true)
                }
            })
        }
    }

    private fun setDataRvListPromoShop() {
        listShopViewModel.getListPromoShop(true).observe(viewLifecycleOwner, { listShops ->
            if (listShops != null && listShops.isNotEmpty()) {
                listShopAdapter.setListShops(listShops, user)
                setPromoShopsAdapter()
                showEmptyListShop(false)
            } else {
                showEmptyListShop(true)
            }
        })
    }

    private fun setPromoShopsAdapter() {
        with(binding.rvListMitra) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = listShopAdapter
            Log.d(TAG, "setAdapter: ")
        }
    }

    private fun showEmptyListShop(state: Boolean) {
        if (state) {
            binding.rvListMitra.visibility = View.GONE
            binding.linearEmptyMitra.visibility = View.VISIBLE
        } else {
            binding.rvListMitra.visibility = View.VISIBLE
        }
    }

}