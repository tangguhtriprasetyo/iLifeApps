package com.ibunda.mitrailifeapps.ui.dashboard.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.databinding.FragmentHomeBinding
import com.ibunda.mitrailifeapps.ui.dashboard.home.dialogseleksiberdasarkan.DialogSeleksiBerdasarkanFragment


class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ic_message -> openMessage()
            R.id.ic_notification -> openNotifications()
            R.id.linear_sortby -> dialogSeleksiBerdasarkan()
        }
    }

    private fun openMessage() {

    }

    private fun openNotifications() {

    }

    private fun dialogSeleksiBerdasarkan() {
        val mDialogSeleksiBerdasarkanFragment = DialogSeleksiBerdasarkanFragment()
        val mFragmentManager = childFragmentManager
        mDialogSeleksiBerdasarkanFragment.show(mFragmentManager, mDialogSeleksiBerdasarkanFragment::class.java.simpleName)
    }

    internal var optionDialogListener: DialogSeleksiBerdasarkanFragment.OnOptionDialogListener = object : DialogSeleksiBerdasarkanFragment.OnOptionDialogListener {
        override fun onOptionChosen(category: String?) {
            val kategoriMitra: String? = category

        }
    }

}