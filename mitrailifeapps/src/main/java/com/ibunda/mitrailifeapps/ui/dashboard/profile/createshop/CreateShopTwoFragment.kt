package com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.FragmentCreateShopTwoBinding
import com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop.dialogkategorishop.DialogKategoriShopFragment
import com.ibunda.mitrailifeapps.ui.maps.MapsActivity
import com.ibunda.mitrailifeapps.ui.maps.MapsViewModel

class CreateShopTwoFragment : Fragment() {

    private lateinit var binding: FragmentCreateShopTwoBinding

    private val mapsViewModel: MapsViewModel by activityViewModels()

    private lateinit var shopsData: Shops

    companion object {
        const val EXTRA_SHOP = "extra_shop"
    }

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

        val bundle = arguments
        if (bundle != null) {
            shopsData = bundle.getParcelable(EXTRA_SHOP)!!
        }

        initView()

    }

    private fun initView() {

        binding.icBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

        binding.etLokasiToko.setOnClickListener {
            openMaps()
        }

        binding.etKategoriJasa.setOnClickListener {
            dialogKategoriShop()
        }

        binding.btnBuatToko.setOnClickListener {
            createShop()
        }

    }

    private fun createShop() {

        mapsViewModel.shopAdress.observe(viewLifecycleOwner, Observer {
            binding.etLokasiToko.setText(mapsViewModel.shopAdress.value)
        })
        mapsViewModel.shopLatitude.observe(viewLifecycleOwner, Observer {
            Log.e(mapsViewModel.shopLatitude.value.toString(), "shopLatitude")
        })
        mapsViewModel.shopLongitude.observe(viewLifecycleOwner, Observer {
            Log.e(mapsViewModel.shopLongitude.value.toString(), "shopLangitude")
        })

    }

    private fun openMaps() {
        val intent =
            Intent(requireActivity(), MapsActivity::class.java)
        startActivity(intent)
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
                binding.etKategoriJasa.setText(kategoriMitra)
            }
        }

    private fun validateInput(): Boolean {

        val categoryName = binding.etKategoriJasa.text.toString().trim()
        val address = binding.etLokasiToko.text.toString().trim()
        val price = binding.etHargaJasa.text.toString().trim()
        val kemampuan1 = binding.etHargaJasa.text.toString().trim()
        val kemampuan2 = binding.etHargaJasa.text.toString().trim()
        val kemampuan3 = binding.etHargaJasa.text.toString().trim()

        return when {
            categoryName.isEmpty() -> {
                binding.etKategoriJasa.error = "Kategori Toko tidak boleh kosong."
                false
            }
            address.isEmpty() -> {
                binding.etLokasiToko.error = "Alamat Toko tidak boleh kosong."
                false
            }
            price.isEmpty() -> {
                binding.etHargaJasa.error = "Harga Toko tidak boleh kosong."
                false
            }
            kemampuan1.isEmpty() -> {
                binding.etKemampuan1.error = "Kemampuan 1 tidak boleh kosong."
                false
            }
            kemampuan2.isEmpty() -> {
                binding.etKemampuan2.error = "Kemampuan 2 tidak boleh kosong."
                false
            }
            kemampuan3.isEmpty() -> {
                binding.etKemampuan3.error = "Kemampuan 3 tidak boleh kosong."
                false
            }
            else -> {
                true
            }
        }

    }


}