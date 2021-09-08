package com.ibunda.mitrailifeapps.ui.dashboard.profile.editshop

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.FragmentEditShopBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.utils.ProgressDialogHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class EditShopFragment : Fragment() {

    private lateinit var binding : FragmentEditShopBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var shopsDataProfile: Shops

    private var valueType: String? = null
    private var uriImagePath: Uri? = null

    companion object {
        const val EXTRA_EDIT = "extra_edit"
    }

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

        val getImage =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uriImage ->
                if (uriImage.path != null) {
                    uriImagePath = uriImage
                    binding.imgTokoMitra.setImageURI(uriImagePath)
                }
            }

        initView()
        getShopData()

    }

    private fun initView() {

        val bottomNav: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE

        binding.icBack.setOnClickListener {
            bottomNav.visibility = View.VISIBLE
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    //Initialize Shop Data
    private fun getShopData() {
        mainViewModel.getShopData()
            .observe(viewLifecycleOwner, { shopsProfile ->
                if (shopsProfile != null) {
                    shopsDataProfile = shopsProfile
                    val bundle = arguments
                    if (bundle != null) {
                        valueType = bundle.getString(EXTRA_EDIT)
                    }
                    if (valueType == "EditShop") {
                        initEditShop(shopsDataProfile)
                    } else {
                        initEditKemampuan(shopsDataProfile)
                    }
                }
                Log.d("ViewModelShopsProfile: ", shopsProfile.toString())
            })
    }

    //Update Shop Kemmapuan
    private fun initEditKemampuan(shopsDataProfile : Shops) {
        with(binding) {
            tvTitle.text = getString(R.string.harga_kemampuan)
            etHargaJasa.setText(shopsDataProfile.price.toString())
            etKemampuan1.setText(shopsDataProfile.kemampuan1)
            etKemampuan2.setText(shopsDataProfile.kemampuan2)
            etKemampuan3.setText(shopsDataProfile.kemampuan3)
            linearEditKemampuan.visibility = View.VISIBLE
            btnSimpanKemampuan.visibility = View.VISIBLE
            btnSimpanKemampuan.setOnClickListener{
                if (validateInputEditKemampuan()) {
                    updateShopKemampuan()
                }
            }
        }
    }
    private fun validateInputEditKemampuan(): Boolean {

        val price = binding.etHargaJasa.text.toString().trim()
        val kemampuan1 = binding.etKemampuan1.text.toString().trim()
        val kemampuan2 = binding.etKemampuan2.text.toString().trim()
        val kemampuan3 = binding.etKemampuan3.text.toString().trim()

        return when {
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
    private fun updateShopKemampuan() {
        with(binding) {
            shopsDataProfile.price = etHargaJasa.text.toString().toInt()
            shopsDataProfile.kemampuan1 = etKemampuan1.text.toString()
            shopsDataProfile.kemampuan2 = etKemampuan2.text.toString()
            shopsDataProfile.kemampuan3 = etKemampuan3.text.toString()
        }
        uploadShopData()
    }

    //Update Shop Data
    private fun initEditShop(shopsDataProfile : Shops) {
        with(binding) {
            etNamaToko.setText(shopsDataProfile.shopName)
            etFacebook.setText(shopsDataProfile.facebook)
            etInstagram.setText(shopsDataProfile.instagram)
            tvTitle.text = getString(R.string.edit_informasi_toko)
            linearEditShop.visibility = View.VISIBLE
            btnSimpanShop.visibility = View.VISIBLE
            btnSimpanShop.visibility = View.VISIBLE
            btnSimpanShop.setOnClickListener{
                if (validateInputEditShop()) {
                    updateShopData()
                    progressDialog(true)
                }
            }
        }
    }
    private fun validateInputEditShop(): Boolean {

        val shopName = binding.etNamaToko.text.toString().trim()
        val instagram = binding.etInstagram.text.toString().trim()
        val facebook = binding.etFacebook.text.toString().trim()

        return when {
            shopName.length < 5 -> {
                binding.etNamaToko.error = "Minimal Nama Toko adalah 5 huruf."
                false
            }
            shopName.length > 35 -> {
                binding.etNamaToko.error = "Maksimal Nama Toko adalah 35 huruf."
                false
            }
            facebook.isEmpty() -> {
                binding.etFacebook.error = "Nama Pengguna Facebook tidak boleh kosong."
                false
            }
            facebook.contains(" ") -> {
                binding.etFacebook.error = "Nama Pengguna Facebook tidak boleh menggunakan spasi."
                false
            }
            instagram.isEmpty() -> {
                binding.etInstagram.error = "Username Instagram tidak boleh kosong."
                false
            }
            instagram.contains(" ") -> {
                binding.etInstagram.error = "Username Instagram tidak boleh menggunakan spasi."
                false
            }
            else -> {
                true
            }
        }

    }
    private fun uploadShopsPicture() {
        TODO("Not yet implemented")
    }
    private fun updateShopData() {
        with(binding) {
            shopsDataProfile.shopName = etNamaToko.text.toString()
            shopsDataProfile.facebook = etFacebook.text.toString()
            shopsDataProfile.instagram = etInstagram.text.toString()
        }
        if (uriImagePath != null) {
            uploadShopsPicture()
        } else {
            uploadShopData()
        }
    }

    //Upload Shop Data
    private fun uploadShopData() {
        mainViewModel.editShopData(shopsDataProfile).observe(viewLifecycleOwner, { newShopData ->
            if (newShopData != null) {
                Toast.makeText(
                    requireActivity(),
                    "Shop Data Successfull Updated",
                    Toast.LENGTH_SHORT
                ).show()
                progressDialog(false)
                val bottomNav: BottomNavigationView =
                    requireActivity().findViewById(R.id.bottom_navigation)
                bottomNav.visibility = View.VISIBLE
                requireActivity().supportFragmentManager.popBackStackImmediate()
            } else {
                progressDialog(false)
                Toast.makeText(
                    requireActivity(),
                    "Update Shop Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun progressDialog(state: Boolean) {
        val dialog = ProgressDialogHelper.setProgressDialog(requireContext(), "Loading...")
        if (state) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }

}