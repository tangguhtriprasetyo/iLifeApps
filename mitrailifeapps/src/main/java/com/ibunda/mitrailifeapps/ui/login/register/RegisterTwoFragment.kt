package com.ibunda.mitrailifeapps.ui.login.register

import android.app.Dialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.databinding.FragmentRegisterTwoBinding
import com.ibunda.mitrailifeapps.ui.login.LoginActivity
import com.ibunda.mitrailifeapps.ui.login.LoginViewModel
import com.ibunda.mitrailifeapps.utils.DateHelper
import com.ibunda.mitrailifeapps.utils.ProgressDialogHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class RegisterTwoFragment : Fragment() {

    private lateinit var binding : FragmentRegisterTwoBinding
    private lateinit var mAuth: FirebaseAuth

    private val loginViewModel: LoginViewModel by activityViewModels()

    private lateinit var mitrasData: Mitras
    private lateinit var progressDialog : Dialog

    companion object {
        const val EXTRA_USER = "extra_user"
        const val PREFS_NAME = "mitra_pref"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialogHelper.progressDialog(requireContext())

        mAuth = Firebase.auth

        val bundle = arguments
        if (bundle != null) {
            mitrasData = bundle.getParcelable(EXTRA_USER)!!
        }

        binding.icBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

        binding.btnRegister.setOnClickListener {
            if (validateName()) {
                registerMitra(mitrasData.email.toString(), mitrasData.password.toString())
            }
        }

    }

    private fun registerMitra(email: String, password: String) {
        progressDialog.show()

        val provinsi = binding.etProvinsi.text.toString().trim()
        val kotakab = binding.etKotaKab.text.toString().trim()
        val kecamatan = binding.etKecamatan.text.toString().trim()
        val kodepos = binding.etKodepos.text.toString().trim()
        val alamat = binding.etAlamatLengkap.text.toString().trim()

        /*create a user*/
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val user = mAuth.currentUser
                    val mitraId = user!!.uid

                    val mitras = Mitras(
                        mitraId = mitraId,
                        name = mitrasData.name,
                        email = mitrasData.email,
                        provinsi = provinsi,
                        kotakab = kotakab,
                        kecamatan = kecamatan,
                        kodepos = kodepos,
                        address = alamat,
                        totalShop = 0,
                        registeredAt = DateHelper.getCurrentDate(),
                        verified = false,
                    )
                    createNewUser(mitras)
                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun createNewUser(mitras: Mitras) {
        Log.d("createdNewUser", mitras.name.toString())
        loginViewModel.createdNewUser(mitras).observe(viewLifecycleOwner, { newUser ->
            if (newUser.isCreated == true) {
                progressDialog.dismiss()
                Log.d(TAG, "Hello ${mitras.name}, Your Account Successfully Created!")
                val user = mAuth.currentUser
                //Preference
                val preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putInt("totalShop", mitras.totalShop!!)
                editor.apply()

                sendEmailVerification(user)
            }
        })
    }

    private fun sendEmailVerification(user: FirebaseUser?) {
        user!!.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // email sent
                        val dialog = AlertDialog.Builder(requireContext(), R.style.Theme_AppCompat_DayNight_Dialog).create()
                        dialog.setTitle("Registrasi Berhasil")
                        dialog.setMessage("Silahkan untuk verifikasi email anda agar dapat masuk ke Mitra i-life.")
                        dialog.setButton(Dialog.BUTTON_POSITIVE, "Ok") { dialog, which ->
                            mAuth.signOut()
                            val intent =
                                    Intent(requireActivity(), LoginActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                        dialog.show()
                        dialog.setCancelable(false)
                        dialog.setCanceledOnTouchOutside(false)
                        Log.d(TAG, "Email sent.")
                    }
                }
    }

    private fun validateName(): Boolean {

        val provinsi = binding.etProvinsi.text.toString().trim()
        val kotakab = binding.etKotaKab.text.toString().trim()
        val kecamatan = binding.etKecamatan.text.toString().trim()
        val kodepos = binding.etKodepos.text.toString().trim()
        val alamat = binding.etAlamatLengkap.text.toString().trim()

        return when {
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


}