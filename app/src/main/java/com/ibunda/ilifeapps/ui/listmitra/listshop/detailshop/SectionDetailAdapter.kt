package com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.kemampuan.KemampuanFragment
import com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.ulasan.UlasanFragment

class SectionDetailAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return KemampuanFragment()
                1 -> return UlasanFragment()
            }
            return Fragment()
        }

    }
