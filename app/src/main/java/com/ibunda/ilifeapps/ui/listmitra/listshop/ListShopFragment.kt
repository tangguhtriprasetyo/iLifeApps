package com.ibunda.ilifeapps.ui.listmitra.listshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.PagedList
import com.ibunda.ilifeapps.databinding.FragmentListShopBinding


class ListShopFragment : Fragment() {
    private lateinit var binding: FragmentListShopBinding
    private val listShopViewModel: ListShopViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataRvListShop()
    }

    private fun setDataRvListShop() {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(2)
            .setPageSize(4)
            .build()
//
//        val options = FirestorePagingOptions.Builder<Users>()
//            .setLifecycleOwner(this)
//            .setQuery(mQuery, config, Post::class.java)
//            .build()
    }
}