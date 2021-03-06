package com.ibunda.mitrailifeapps.ui.dashboard.profile.setting.editakunmitra

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.databinding.FragmentEditAkunMitraBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.utils.AppConstants.STATUS_SUCCESS
import com.ibunda.mitrailifeapps.utils.ProgressDialogHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class EditAkunMitraFragment : Fragment() {

    private lateinit var binding: FragmentEditAkunMitraBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var mitraDataProfile: Mitras

    private var valueType: String? = null
    private var emailMitra: String? = null

    private lateinit var progressDialog : Dialog

    companion object {
        const val EXTRA_NAME = "extra_name"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditAkunMitraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialogHelper.progressDialog(requireContext())

        initView()
        getProfileData()

        val bundle = arguments
        if (bundle != null) {
            valueType = bundle.getString(EXTRA_NAME)
        }
        if (valueType == "EditAkun") {
            initEditAkun()
        } else {
            initChangePassword()
        }

    }

    private fun getProfileData() {
        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    mitraDataProfile = userProfile
                    setProfileData(mitraDataProfile)
                    emailMitra = mitraDataProfile.email
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })
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

    private fun updateProfileData() {
        progressDialog.show()
        with(binding) {
            mitraDataProfile.name = etUsername.text.toString()
            mitraDataProfile.provinsi = etProvinsi.text.toString()
            mitraDataProfile.kotakab = etKotaKab.text.toString()
            mitraDataProfile.kecamatan = etKecamatan.text.toString()
            mitraDataProfile.kodepos = etKodepos.text.toString()
            mitraDataProfile.address = etAlamatLengkap.text.toString()
        }
        uploadData()
    }

    private fun uploadData() {
        mainViewModel.editProfileMitra(mitraDataProfile).observe(viewLifecycleOwner, { newUserData ->
            if (newUserData != null) {
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Profile Successfull Updated",
                    Toast.LENGTH_SHORT
                ).show()
                val bottomNav: BottomNavigationView =
                    requireActivity().findViewById(R.id.bottom_navigation)
                bottomNav.visibility = View.VISIBLE
                requireActivity().supportFragmentManager.popBackStackImmediate()
            } else {
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Update Profile Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun initEditAkun() {
        binding.tvTitle.text = getString(R.string.edit_informasi_akun)
        binding.linearEditAkunMitra.visibility = View.VISIBLE
        binding.etEmail.setOnClickListener {
            Toast.makeText(requireContext(), "Email tidak dapat diubah.", Toast.LENGTH_SHORT).show()
        }
        binding.btnSaveChange.setOnClickListener {
            if (validateInput()) {
                updateProfileData()
            }
        }
    }

    private fun initChangePassword() {
        binding.tvTitle.text = getString(R.string.ubah_kata_sandi)
        binding.linearEditPassword.visibility = View.VISIBLE
        binding.btnChangePassword.setOnClickListener {
            if (validateInputPassword()) {
                updatePasswordData()
            }
        }
    }

    private fun updatePasswordData() {
        progressDialog.show()
        val recentPass = binding.etRecentPassword.text.toString().trim()
        val newPass = binding.etNewPassword.text.toString().trim()

        mainViewModel.updatePasswordMitra(emailMitra.toString(), recentPass, newPass).observe(viewLifecycleOwner, { status ->
            if (status == STATUS_SUCCESS) {
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Kata Sandi berhasil diperbarui.",
                    Toast.LENGTH_SHORT
                ).show()
                val bottomNav: BottomNavigationView =
                    requireActivity().findViewById(R.id.bottom_navigation)
                bottomNav.visibility = View.VISIBLE
                requireActivity().supportFragmentManager.popBackStackImmediate()
            } else {
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    status,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun setProfileData(mitraDataProfile: Mitras) {
        with(binding) {
            etUsername.setText(mitraDataProfile.name)
            etEmail.setText(mitraDataProfile.email)
            etProvinsi.setText(mitraDataProfile.provinsi)
            etKotaKab.setText(mitraDataProfile.kotakab)
            etKecamatan.setText(mitraDataProfile.kecamatan)
            etKodepos.setText(mitraDataProfile.kodepos)
            etAlamatLengkap.setText(mitraDataProfile.address)
        }
    }

    private fun validateInput(): Boolean {

        val userName = binding.etUsername.text.toString().trim()
        val provinsi = binding.etProvinsi.text.toString().trim()
        val kotakab = binding.etKotaKab.text.toString().trim()
        val kecamatan = binding.etKecamatan.text.toString().trim()
        val kodepos = binding.etKodepos.text.toString().trim()
        val alamat = binding.etAlamatLengkap.text.toString().trim()

        return when {
            userName.length < 6 -> {
                binding.etUsername.error = "Minimal Nama Pengguna adalah 6 huruf."
                false
            }
            userName.length > 20 -> {
                binding.etUsername.error = "Maksimal Nama Pengguna adalah 20 huruf."
                false
            }
            userName.contains(" ") -> {
                binding.etUsername.error = "Nama Pengguna tidak boleh menggunakan spasi."
                false
            }
            provinsi.length < 3 -> {
                binding.etProvinsi.error = "Masukkan provinsi dengan benar."
                false
            }
            kotakab.length < 3 -> {
                binding.etKotaKab.error = "Masukkan kota/kabupaten dengan benar."
                false
            }
            kecamatan.length < 3 -> {
                binding.etKecamatan.error = "Masukkan kecamatan dengan benar."
                false
            }
            kodepos.length < 3 -> {
                binding.etKodepos.error = "Masukkan kodepos dengan benar."
                false
            }
            alamat.length < 3 -> {
                binding.etAlamatLengkap.error = "Masukkan alamat dengan benar."
                false
            }
            else -> {
                true
            }
        }

    }

    private fun validateInputPassword(): Boolean {

        val recentPass = binding.etRecentPassword.text.toString().trim()
        val newPass = binding.etNewPassword.text.toString().trim()
        val confirmPass = binding.etConfirmPassword.text.toString().trim()

        return when {
            recentPass.isEmpty() -> {
                binding.etRecentPassword.error = "Kata Sandi Lama tidak boleh kosong."
                false
            }
            newPass.isEmpty() -> {
                binding.etNewPassword.error = "Kata Sandi Baru tidak boleh kosong."
                false
            }
            confirmPass.isEmpty() -> {
                binding.etConfirmPassword.error = "Konfirmasi Kata Sandi tidak boleh kosong."
                false
            }
            newPass.length < 6 -> {
                binding.etNewPassword.error = "Minimal Kata Sandi adalah 6 huruf."
                false
            }
            newPass != confirmPass -> {
                Toast.makeText(requireContext(), "Kata Sandi baru yang dimasukkan tidak sama.", Toast.LENGTH_SHORT).show()
                false
            }
            recentPass.contains(" ") -> {
                binding.etRecentPassword.error = "Kata Sandi tidak boleh menggunakan spasi."
                false
            }
            newPass.contains(" ") -> {
                binding.etNewPassword.error = "Kata Sandi tidak boleh menggunakan spasi."
                false
            }
            confirmPass.contains(" ") -> {
                binding.etConfirmPassword.error = "Kata Sandi tidak boleh menggunakan spasi."
                false
            }
            else -> {
                true
            }
        }

    }



}