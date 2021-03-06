package com.ibunda.mitrailifeapps.ui.dashboard.transaction

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SectionTransactionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return ContentTransactionFragment.newInstance(position + 1)
    }

    override fun getItemCount(): Int {
        return 3
    }
}