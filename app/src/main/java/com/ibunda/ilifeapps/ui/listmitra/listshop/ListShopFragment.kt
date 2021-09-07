package com.ibunda.ilifeapps.ui.listmitra.listshop

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentListShopBinding
import com.ibunda.ilifeapps.ui.listmitra.ListMitraViewModel
import com.ibunda.ilifeapps.ui.listmitra.listshop.dialogseleksiberdasarkan.DialogSeleksiBerdasarkanFragment


class ListShopFragment : Fragment() {

    private lateinit var binding: FragmentListShopBinding
    private lateinit var user: Users

    private val listShopViewModel: ListShopViewModel by activityViewModels()
    private val listMitraViewModel: ListMitraViewModel by activityViewModels()
    private val listShopAdapter = ListShopAdapter()
    private var category: String? = null
    private var search: String? = null
    private var promo: Boolean = false
    private var filter: String? = null

    companion object {
        const val EXTRA_PROMO = "extra_promo"
        const val EXTRA_SEARCH = "extra_search"
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
        initOnClick()
        setFilterQuery()

    }

    private fun setFilterQuery() {
        listShopViewModel.filterQuery.observe(viewLifecycleOwner, { filterQuery ->
            if (filterQuery != null) {
                filter = filterQuery
                setDataRvListShop()
            }
        })
    }

    private fun initOnClick() {
        binding.linearSortby.setOnClickListener {
            val mFragmentManager = parentFragmentManager
            val mDialogSeleksiBerdasarkan = DialogSeleksiBerdasarkanFragment()
            mDialogSeleksiBerdasarkan.show(mFragmentManager, "DialogSeleksiBerdasarkanFragment")
        }
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
                promo = requireArguments().getBoolean(EXTRA_PROMO, false)
                search = requireArguments().getString(EXTRA_SEARCH, null)
                setDataRvListShop()
            }
        })
    }

    private fun setDataRvListShop() {
        listMitraViewModel.dataCategory.observe(viewLifecycleOwner, { dataCategory ->
            if (dataCategory != null) {
                binding.tvListKategoriMitra.text = listMitraViewModel.dataCategory.value
                category = dataCategory
            }
        })

        listShopViewModel.getListShop(category, promo, search)
            .observe(viewLifecycleOwner, { listShops ->
                if (listShops != null && listShops.isNotEmpty()) {
                    listShopAdapter.setListShops(listShops, user, filter)
                    Log.d(TAG, "setDataRvListShop: $filter")
                    setShopsAdapter()
                    showEmptyListShop(false)
                } else {
                    showEmptyListShop(true)
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

    private fun showEmptyListShop(state: Boolean) {
        if (state) {
            binding.rvListMitra.visibility = View.GONE
            binding.linearEmptyMitra.visibility = View.VISIBLE
        } else {
            binding.rvListMitra.visibility = View.VISIBLE
        }
    }

}