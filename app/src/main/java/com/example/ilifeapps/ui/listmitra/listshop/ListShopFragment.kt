package com.example.ilifeapps.ui.listmitra.listshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ilifeapps.databinding.FragmentListShopBinding


class ListShopFragment : Fragment() {
    private lateinit var binding : FragmentListShopBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListShopBinding.inflate(inflater, container, false)
        return binding.root
    }
}