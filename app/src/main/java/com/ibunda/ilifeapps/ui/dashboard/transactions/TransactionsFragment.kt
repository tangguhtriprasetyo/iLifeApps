package com.ibunda.ilifeapps.ui.dashboard.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.ibunda.ilifeapps.databinding.FragmentTransactionsBinding


class TransactionsFragment : Fragment() {

    private lateinit var binding : FragmentTransactionsBinding

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): TransactionsFragment {
            return TransactionsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayout()
    }

    private fun initTabLayout() {
        val sectionAdapter = SectionTransactionsAdapter(this)
        binding.viewPager.adapter = sectionAdapter
        binding.viewPager.isSaveEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Pesanan"
                1 -> tab.text = "Diproses"
                2 -> tab.text = "Selesai"
                3 -> tab.text = "Dibatalkan"
            }
        }.attach()
    }
}