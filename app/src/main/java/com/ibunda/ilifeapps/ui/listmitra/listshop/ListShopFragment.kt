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
import com.ibunda.ilifeapps.databinding.FragmentListShopBinding


class ListShopFragment : Fragment() {
    private lateinit var binding: FragmentListShopBinding
    private val listShopViewModel: ListShopViewModel by activityViewModels()
    private val listShopAdapter = ListShopAdapter()

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
        setShopsAdapter()
        setDataRvListShop()
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
        listShopViewModel.getListShop("Kebersihan").observe(viewLifecycleOwner, { listShops ->
            if (listShops != null) {
                listShopAdapter.setListShops(listShops)
            }
        })
    }
}