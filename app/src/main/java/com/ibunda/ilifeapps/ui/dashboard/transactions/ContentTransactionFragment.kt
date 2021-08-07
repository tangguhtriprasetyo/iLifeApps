package com.ibunda.ilifeapps.ui.dashboard.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibunda.ilifeapps.databinding.FragmentContentTransactionBinding


class ContentTransactionFragment : Fragment() {

    private lateinit var binding: FragmentContentTransactionBinding

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(index: Int) =
            ContentTransactionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContentTransactionBinding.inflate(inflater, container, false)

        when (arguments?.getInt(ARG_SECTION_NUMBER, 0)) {
            1 -> {
                binding.tvTesTab.text = "pesanan"
            }//pesanan

            2 -> {
                binding.tvTesTab.text = "diproses"
            }//diproses

            3 -> {
                binding.tvTesTab.text = "selesai"
            }//selesai

            4 -> {
                binding.tvTesTab.text = "dibatalkan"
            }//dibatalkan
        }

        return binding.root
    }

}