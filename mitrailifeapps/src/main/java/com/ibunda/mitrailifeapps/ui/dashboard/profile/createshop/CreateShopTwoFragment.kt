package com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.FragmentCreateShopTwoBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainActivity
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop.dialogkategorishop.DialogKategoriShopFragment
import com.ibunda.mitrailifeapps.ui.maps.MapsActivity
import com.ibunda.mitrailifeapps.utils.DateHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.math.roundToInt

@ExperimentalCoroutinesApi
class CreateShopTwoFragment : Fragment() {

    private lateinit var binding: FragmentCreateShopTwoBinding

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var shopsData: Shops
    private lateinit var mitraDataProfile: Mitras


    private var address: String? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    address = result.data?.getStringExtra("address")
                    latitude = result.data?.getDoubleExtra("latitude", 0.0)
                    longitude = result.data?.getDoubleExtra("longitude", 0.0)
                    binding.etLokasiToko.setText(address)
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    val status = result.data?.let { Autocomplete.getStatusFromIntent(it) }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
        }

    companion object {
        const val EXTRA_SHOP = "extra_shop"
        const val PREFS_NAME = "mitra_pref"
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

        mainViewModel.getProfileData()
                .observe(viewLifecycleOwner, { userProfile ->
                    if (userProfile != null) {
                        mitraDataProfile = userProfile
                        initView(mitraDataProfile)
                    }
                    Log.d("ViewModelProfile: ", userProfile.toString())
                })

        val bundle = arguments
        if (bundle != null) {
            shopsData = bundle.getParcelable(EXTRA_SHOP)!!
        }
        Log.e(shopsData.shopName, "shopName")

    }

    private fun initView(mitraDataProfile: Mitras) {

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
            if (validateInput()) {
                createShop(mitraDataProfile)
            }
        }


    }

    private fun createShop(mitraDataProfile : Mitras) {
        //Preference
        val preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var totalShops = preferences.getInt("totalShop", 0)
        totalShops+=1

        Log.e(totalShops.toString(), "totalShops")
        //End Preference
        val shopId = mitraDataProfile.mitraId + String.format("TOKO%s", String.format("%01d", totalShops.toDouble().roundToInt()))
        Log.e(shopId, "shopId")
        shopsData = Shops(
                shopId = shopId,
                address = binding.etLokasiToko.text.toString(),
                categoryName = binding.etKategoriJasa.text.toString(),
                facebook = shopsData.facebook,
                instagram = shopsData.instagram,
                promo = false,
                verified = false,
                kemampuan1 = binding.etKemampuan1.text.toString(),
                kemampuan2 = binding.etKemampuan2.text.toString(),
                kemampuan3 = binding.etKemampuan3.text.toString(),
                latitude = latitude,
                longitude = longitude,
                mitraId = mitraDataProfile.mitraId,
                price = binding.etHargaJasa.text.toString().toInt(),
                rating = 0.toDouble(),
                registeredAt = DateHelper.getCurrentDate(),
                shopName = shopsData.shopName,
                shopPicture = shopsData.shopPicture,
                shopPromo = 0,
                totalPesananSukses = 0,
                totalUlasan = 0
        )
        val editor = preferences.edit()
        editor.putString("shopId", shopId)
        editor.putInt("totalShop", totalShops)
        editor.apply()
        uploadShops(shopsData)
    }

    private fun uploadShops(shopsData: Shops) {
        Log.d("createdNewUser", shopsData.shopName.toString())
        mainViewModel.createdNewShop(shopsData).observe(viewLifecycleOwner, { newUser ->
            if (newUser.isCreated == true) {
                val intent =
                Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
                Toast.makeText(
                        requireContext(),
                        "Selamat anda telah berhasil membuat toko baru. Silahkan melihat pekerjaan dan pesanan dari customer.",
                        Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun openMaps() {
        getResult.launch(Intent(requireActivity(), MapsActivity::class.java))
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
        val kemampuan1 = binding.etKemampuan1.text.toString().trim()
        val kemampuan2 = binding.etKemampuan2.text.toString().trim()
        val kemampuan3 = binding.etKemampuan3.text.toString().trim()

        return when {
            categoryName.isEmpty() -> {
                binding.etKategoriJasa.error = "Kategori Toko tidak boleh kosong."
                false
            }
//            address.isEmpty() -> {
//                binding.etLokasiToko.error = "Alamat Toko tidak boleh kosong."
//                false
//            }
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