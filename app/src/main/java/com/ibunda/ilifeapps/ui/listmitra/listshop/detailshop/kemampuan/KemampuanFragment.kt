package com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.kemampuan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.databinding.FragmentKemampuanBinding
import com.ibunda.ilifeapps.ui.listmitra.ListMitraViewModel

class KemampuanFragment : Fragment() {

    private lateinit var binding : FragmentKemampuanBinding

    private val listMitraViewModel: ListMitraViewModel by activityViewModels()
    private lateinit var shopData: Shops

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentKemampuanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getKemampuanData()

    }

    private fun getKemampuanData() {
        listMitraViewModel.getShopData()
            .observe(viewLifecycleOwner, { shops ->
                if (shops != null) {
                    shopData = shops
                    setKemampuan(shopData)
                }
                Log.d("ViewModelShopData: ", shops.toString())
            })
    }

    private fun setKemampuan(shopData: Shops) {
        with(binding) {
            tvKemampuan1.text = shopData.kemampuan1
            tvKemampuan2.text = shopData.kemampuan2
            tvKemampuan3.text = shopData.kemampuan3
        }
    }
}