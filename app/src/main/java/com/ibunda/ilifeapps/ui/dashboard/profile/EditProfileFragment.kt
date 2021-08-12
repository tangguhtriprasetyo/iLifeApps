package com.ibunda.ilifeapps.ui.dashboard.profile

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.DialogJenisKelaminBinding
import com.ibunda.ilifeapps.databinding.FragmentEditProfileBinding
import com.ibunda.ilifeapps.ui.dashboard.MainViewModel
import com.ibunda.ilifeapps.utils.DatePickerHelper
import com.ibunda.ilifeapps.utils.loadImage
import java.util.*


class EditProfileFragment : Fragment() {

    private lateinit var binding : FragmentEditProfileBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var userDataProfile: Users
    lateinit var datePicker: DatePickerHelper

    private var uriImagePath: Uri? = null
    private var gender: String? = null

    companion object {
        const val EXTRA_DATA_USER = "extra_data_user"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePicker = DatePickerHelper(requireContext())
        val getImage =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uriImage ->
                if (uriImage.path != null) {
                    uriImagePath = uriImage
                    binding.imgProfile.setImageURI(uriImagePath)
                }
            }

        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                    setProfileData(userDataProfile)
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })

        val bottomNav: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE

        binding.icBack.setOnClickListener {
            bottomNav.visibility = View.VISIBLE
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

        binding.imgChangePicture.setOnClickListener {
            getImage.launch(arrayOf("image/*"))
        }

        binding.etJenisKelamin.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val binding : DialogJenisKelaminBinding = DialogJenisKelaminBinding.inflate(LayoutInflater.from(context))
            dialog.setContentView(binding.root)
            binding.btnPilihGender.setOnClickListener {
                val checkedRadioButtonId = binding.rgGender.checkedRadioButtonId
                if (checkedRadioButtonId != 1) {
                    when (checkedRadioButtonId) {
                        R.id.rb_laki -> gender = binding.rbLaki.text.toString().trim()
                        R.id.rb_perempuan -> gender = binding.rbPerempuan.text.toString().trim()
                    }
                }
                jeniskelamin(gender)
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.etTanggalLahir.setOnClickListener {
            val cal = Calendar.getInstance()
            val d = cal.get(Calendar.DAY_OF_MONTH)
            val m = cal.get(Calendar.MONTH)
            val y = cal.get(Calendar.YEAR)
            datePicker.setMaxDate(cal.timeInMillis)
            datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
                override fun onDateSelected(datePicker: View, dayofMonth: Int, month: Int, year: Int) {
                    val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                    val mon = month + 1
                    val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                    val date = "${dayStr}/${monthStr}/${year}"
                    binding.etTanggalLahir.setText(date)
                }
            })
        }

        binding.btnSimpan.setOnClickListener {
            updateProfileData()
        }

    }

    private fun jeniskelamin(gender: String?) {
        binding.etJenisKelamin.setText(gender)
    }

    private fun setProfileData(userDataProfile: Users) {
        with(binding) {
            imgProfile.loadImage(userDataProfile.avatar)
            etNamaUser.setText(
                if (userDataProfile.name == null) {
                    "-"
                } else {
                    userDataProfile.name
                }
            )
            etEmailUser.setText(
                if (userDataProfile.email == null) {
                    "-"
                } else {
                    userDataProfile.email
                }
            )
            etNoHpUser.setText(
                if (userDataProfile.phone == null) {
                    "-"
                } else {
                    userDataProfile.phone
                }
            )
            etJenisKelamin.setText(
                if (userDataProfile.gender == null) {
                    "-"
                } else {
                    userDataProfile.gender
                }
            )
            etTanggalLahir.setText(
                if (userDataProfile.ttl == null) {
                    "-"
                } else {
                    userDataProfile.ttl
                }
            )
        }
    }

    private fun updateProfileData() {
        with(binding) {
            userDataProfile.name = etNamaUser.text.toString()
            userDataProfile.email = etEmailUser.text.toString()
            userDataProfile.phone = etNoHpUser.text.toString()
            userDataProfile.gender = etJenisKelamin.text.toString()
            userDataProfile.ttl = etTanggalLahir.text.toString()
        }
        userDataProfile.isNew = false

        if (uriImagePath != null) {
            uploadProfilePicture()
        } else {
            uploadData()
        }
    }

    private fun uploadProfilePicture() {
        mainViewModel.uploadImages(
            uriImagePath!!,
            "${userDataProfile.userId.toString()}${userDataProfile.name}",
            "Images",
            "profilePicture"
        ).observe(viewLifecycleOwner, { downloadUrl ->
            if (downloadUrl != null) {
                userDataProfile.avatar = downloadUrl.toString()
                uploadData()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Update Profile Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun uploadData() {
        mainViewModel.editProfileUser(userDataProfile).observe(viewLifecycleOwner, { newUserData ->
            if (newUserData != null) {
                Toast.makeText(
                    requireActivity(),
                    "Profile Updated",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().supportFragmentManager.popBackStackImmediate()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Update Profile Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


}