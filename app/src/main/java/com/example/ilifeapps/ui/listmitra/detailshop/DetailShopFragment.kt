package com.example.ilifeapps.ui.listmitra.detailshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ilifeapps.databinding.FragmentDetailShopBinding

class DetailShopFragment : Fragment() {
    private lateinit var binding : FragmentDetailShopBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailShopBinding.inflate(inflater, container, false)
        return binding.root
    }
}