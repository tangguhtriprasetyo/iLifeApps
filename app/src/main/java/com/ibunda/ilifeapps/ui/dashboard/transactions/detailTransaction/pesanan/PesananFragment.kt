package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibunda.ilifeapps.databinding.FragmentPesananBinding


class PesananFragment : Fragment() {
    private lateinit var binding : FragmentPesananBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPesananBinding.inflate(inflater, container, false)
        return binding.root
    }
}