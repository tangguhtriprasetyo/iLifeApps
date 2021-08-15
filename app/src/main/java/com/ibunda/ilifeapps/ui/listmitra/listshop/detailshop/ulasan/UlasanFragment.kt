package com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.ulasan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibunda.ilifeapps.databinding.FragmentUlasanBinding

class UlasanFragment : Fragment() {

    private lateinit var binding : FragmentUlasanBinding

    companion object{
        fun newInstance() = UlasanFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUlasanBinding.inflate(inflater, container, false)
        return binding.root
    }
}