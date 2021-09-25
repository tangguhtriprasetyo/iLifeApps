package com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.ulasan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.databinding.FragmentUlasanBinding
import com.ibunda.ilifeapps.ui.listmitra.ListMitraViewModel

class UlasanFragment : Fragment() {

    private lateinit var binding : FragmentUlasanBinding

    private val ulasanViewModel: UlasanViewModel by activityViewModels()
    private val listMitraViewModel: ListMitraViewModel by activityViewModels()
    private val ulasanAdapter = UlasanAdapter()

    private var shopData: Shops? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUlasanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getShopData()
    }

    private fun getShopData() {
        listMitraViewModel.getShopData()
            .observe(viewLifecycleOwner, { shops ->
                if (shops != null) {
                    shopData = shops
                    setDataRvUlasan(shopData!!.shopId)
                }
                Log.d("ViewModelShopData: ", shops.toString())
            })
    }

    private fun setUlasanAdapter() {
        with(binding.rvUlasan) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = ulasanAdapter
            Log.d(android.content.ContentValues.TAG, "setAdapter: ")
        }
    }

    private fun setDataRvUlasan(shopId: String?) {
        ulasanViewModel.getListUlasan(shopId.toString()).observe(viewLifecycleOwner, { listUlasan ->
            if (listUlasan != null && listUlasan.isNotEmpty()) {
                ulasanAdapter.setListUlasan(listUlasan)
                showEmptyUlasan(false)
                setUlasanAdapter()
            } else {
                showEmptyUlasan(true)
            }
        })

    }

    private fun showEmptyUlasan(state: Boolean) {
        if (state) {
            binding.rvUlasan.visibility = View.GONE
            binding.linearEmptyUlasan.visibility = View.VISIBLE
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            binding.rvUlasan.visibility = View.VISIBLE
        }
    }

}