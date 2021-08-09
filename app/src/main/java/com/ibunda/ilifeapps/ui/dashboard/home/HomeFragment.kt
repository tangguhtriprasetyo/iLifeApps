package com.ibunda.ilifeapps.ui.dashboard.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.databinding.FragmentHomeBinding
import com.ibunda.ilifeapps.ui.dashboard.MainActivity
import com.ibunda.ilifeapps.ui.dashboard.home.customorder.CustomOrderFragment
import com.ibunda.ilifeapps.ui.dashboard.home.dialogeditprofile.DialogEditProfileFragment

class HomeFragment : Fragment() {

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

        binding.kategoriLainnya.setOnClickListener {
            val mFragmentManager = parentFragmentManager
            val mCustomOrderFragment = CustomOrderFragment()
            mFragmentManager.commit {
                addToBackStack(null)
                replace(
                    R.id.host_fragment_activity_main,
                    mCustomOrderFragment,
                    MainActivity.CHILD_FRAGMENT
                )
            }
        }

    }

    private fun showDialogEditProfile() {
        val mFragmentManager = parentFragmentManager
        val mDialogEditProfileFragment = DialogEditProfileFragment()
        mDialogEditProfileFragment.isCancelable = false
        mDialogEditProfileFragment.show(mFragmentManager, "DialogEditProfileFragment")
    }
}