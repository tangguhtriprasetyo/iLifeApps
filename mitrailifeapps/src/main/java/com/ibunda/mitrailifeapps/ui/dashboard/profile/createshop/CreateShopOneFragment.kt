package com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.FragmentCreateShopOneBinding


class CreateShopOneFragment : Fragment() {

    private lateinit var binding: FragmentCreateShopOneBinding

    private var uriImagePath: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateShopOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initView()

    }

    private fun initView() {

        val bottomNav: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE
        binding.icBack.setOnClickListener {
            bottomNav.visibility = View.VISIBLE
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }
        val getImage =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uriImage ->
                if (uriImage.path != null) {
                    uriImagePath = uriImage
                    binding.imgTokoMitra.setImageURI(uriImagePath)
                }
            }

        binding.icChangeImage.setOnClickListener {
            getImage.launch(arrayOf("image/*"))
        }

        binding.btnNext.setOnClickListener {
            if (uriImagePath == null) {
                Toast.makeText(requireContext(), "Gambar Toko tidak boleh kosong.", Toast.LENGTH_SHORT).show()
            }
            if (validateInput()) {
                nextCreateShop()
            }
        }

    }

    private fun nextCreateShop() {
        val shopName = binding.etNamaToko.text.toString().trim()
        val instagram = binding.etInstagram.text.toString().trim()
        val facebook = binding.etFacebook.text.toString().trim()

        val shops = Shops(shopPicture = uriImagePath.toString(),
            shopName = shopName,
            instagram = instagram,
            facebook = facebook)

        val mCreateShopTwoFragment = CreateShopTwoFragment()
        val mBundle = Bundle()
        mBundle.putParcelable(CreateShopTwoFragment.EXTRA_SHOP, shops)
        mCreateShopTwoFragment.arguments = mBundle

        val mFragmentManager = parentFragmentManager
        mFragmentManager.commit {
            addToBackStack(null)
            replace(
                R.id.host_fragment_activity_main,
                mCreateShopTwoFragment
            )
        }
    }


    private fun validateInput(): Boolean {

        val shopName = binding.etNamaToko.text.toString().trim()
        val instagram = binding.etInstagram.text.toString().trim()
        val facebook = binding.etFacebook.text.toString().trim()


        return when {
            shopName.length < 5 -> {
                binding.etNamaToko.error = "Minimal Nama Toko adalah 5 huruf."
                false
            }
            shopName.length > 35 -> {
                binding.etNamaToko.error = "Maksimal Nama Pengguna adalah 35 huruf."
                false
            }
            facebook.isEmpty() -> {
                binding.etFacebook.error = "Nama Pengguna Facebook tidak boleh kosong."
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
            facebook.contains(" ") -> {
                binding.etFacebook.error = "Nama Pengguna Facebook tidak boleh menggunakan spasi."
                false
            }
            else -> {
                true
            }
        }

    }
}