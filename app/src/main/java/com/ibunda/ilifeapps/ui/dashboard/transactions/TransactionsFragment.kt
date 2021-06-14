package com.ibunda.ilifeapps.ui.dashboard.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibunda.ilifeapps.databinding.FragmentTransactionsBinding


class TransactionsFragment : Fragment() {
    private lateinit var binding : FragmentTransactionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }
}