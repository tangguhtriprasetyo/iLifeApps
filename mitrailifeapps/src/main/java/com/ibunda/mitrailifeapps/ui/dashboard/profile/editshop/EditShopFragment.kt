package com.ibunda.mitrailifeapps.ui.dashboard.profile.editshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.databinding.FragmentEditShopBinding


class EditShopFragment : Fragment() {

    private lateinit var binding : FragmentEditShopBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        initEditShop()
//        initEditKemampuan()

    }

    private fun initEditKemampuan() {
        binding.tvTitle.text = getString(R.string.harga_kemampuan)
        binding.linearEditKemampuan.visibility = View.VISIBLE
        binding.btnSimpanKemampuan.visibility = View.VISIBLE
    }

    private fun initEditShop() {
        binding.tvTitle.text = getString(R.string.edit_informasi_toko)
        binding.linearEditShop.visibility = View.VISIBLE
        binding.btnSimpanShop.visibility = View.VISIBLE
    }
}