package com.example.ilifeapps.ui.detail.diproses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ilifeapps.databinding.FragmentDiprosesBinding


class DiprosesFragment : Fragment() {
    private lateinit var binding : FragmentDiprosesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiprosesBinding.inflate(inflater, container, false)
        return binding.root
    }
}