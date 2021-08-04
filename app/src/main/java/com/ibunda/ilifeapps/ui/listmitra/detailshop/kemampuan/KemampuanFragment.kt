package com.ibunda.ilifeapps.ui.listmitra.detailshop.kemampuan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibunda.ilifeapps.databinding.FragmentKemampuanBinding

class KemampuanFragment : Fragment() {

    private lateinit var binding : FragmentKemampuanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentKemampuanBinding.inflate(inflater, container, false)
        return binding.root
    }
}