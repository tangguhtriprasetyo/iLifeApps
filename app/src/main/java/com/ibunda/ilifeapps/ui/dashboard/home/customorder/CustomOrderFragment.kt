package com.ibunda.ilifeapps.ui.dashboard.home.customorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.databinding.FragmentCustomOrderBinding
import com.ibunda.ilifeapps.ui.dashboard.home.customorder.dialogkategorimitra.DialogKategoriMitraFragment


class CustomOrderFragment : Fragment() {

    private lateinit var binding : FragmentCustomOrderBinding
    var kategoriMitra: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomOrderBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE

        binding.icBack.setOnClickListener {
            bottomNav.visibility = View.VISIBLE
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

        binding.etKategori.setOnClickListener {
            val mDialogKategoriMitraFragment = DialogKategoriMitraFragment()
            val mFragmentManager = childFragmentManager
            mDialogKategoriMitraFragment.show(mFragmentManager, DialogKategoriMitraFragment::class.java.simpleName)
        }

    }

    internal var optionDialogListener: DialogKategoriMitraFragment.OnOptionDialogListener = object : DialogKategoriMitraFragment.OnOptionDialogListener {
        override fun onOptionChosen(category: String?) {
            Toast.makeText(activity, category, Toast.LENGTH_SHORT).show()
            kategoriMitra = category
            binding.etKategori.setText(kategoriMitra)
        }
    }


}