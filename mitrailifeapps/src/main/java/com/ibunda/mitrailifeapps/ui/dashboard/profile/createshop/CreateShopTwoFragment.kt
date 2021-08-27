package com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibunda.mitrailifeapps.databinding.FragmentCreateShopTwoBinding
import com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop.dialogkategorishop.DialogKategoriShopFragment

class CreateShopTwoFragment : Fragment() {

    private lateinit var binding: FragmentCreateShopTwoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateShopTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etLokasiToko.setOnClickListener {
            openMaps()
        }

        binding.etKategoriJasa.setOnClickListener {
            dialogKategoriShop()
        }

    }

    private fun openMaps() {

    }

    private fun dialogKategoriShop() {

        val mDialogKategoriShopFragment = DialogKategoriShopFragment()
        val mFragmentManager = childFragmentManager
        mDialogKategoriShopFragment.show(
            mFragmentManager,
            mDialogKategoriShopFragment::class.java.simpleName
        )

    }

    internal var optionDialogListener: DialogKategoriShopFragment.OnOptionDialogListener =
        object : DialogKategoriShopFragment.OnOptionDialogListener {
            override fun onOptionChosen(category: String?) {
                val kategoriMitra: String? = category

            }
        }


}