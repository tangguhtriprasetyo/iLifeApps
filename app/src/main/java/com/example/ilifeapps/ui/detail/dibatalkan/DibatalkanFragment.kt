package com.example.ilifeapps.ui.detail.dibatalkan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ilifeapps.databinding.FragmentDibatalkanBinding


class DibatalkanFragment : Fragment() {
    private lateinit var binding : FragmentDibatalkanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDibatalkanBinding.inflate(inflater, container, false)
        return binding.root
    }
}