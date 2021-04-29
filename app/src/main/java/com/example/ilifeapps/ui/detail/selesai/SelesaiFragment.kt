package com.example.ilifeapps.ui.detail.selesai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ilifeapps.databinding.FragmentSelesaiBinding


class SelesaiFragment : Fragment() {
    private lateinit var binding : FragmentSelesaiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelesaiBinding.inflate(inflater, container, false)
        return binding.root
    }
}