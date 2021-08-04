package com.ibunda.ilifeapps.ui.dashboard.transactions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionTransactionsAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return TransactionsFragment.newInstance(position + 1)
    }

    override fun getItemCount(): Int {
        return 2
    }
}