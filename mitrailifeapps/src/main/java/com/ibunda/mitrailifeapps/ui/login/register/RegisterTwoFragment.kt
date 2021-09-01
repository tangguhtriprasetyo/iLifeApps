package com.ibunda.mitrailifeapps.ui.login.register

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.databinding.FragmentRegisterTwoBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainActivity
import com.ibunda.mitrailifeapps.ui.login.LoginViewModel
import com.ibunda.mitrailifeapps.utils.DateHelper

class RegisterTwoFragment : Fragment() {

    private lateinit var binding : FragmentRegisterTwoBinding
    private lateinit var mAuth: FirebaseAuth

    private val loginViewModel: LoginViewModel by activityViewModels()

    private lateinit var mitrasData: Mitras

    companion object {
        const val EXTRA_USER = "extra_user"
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

        mAuth = Firebase.auth

        val bundle = arguments
        if (bundle != null) {
            mitrasData = bundle.getParcelable(EXTRA_USER)!!
        }

        Log.e(mitrasData.email, "Email")
        Log.e(mitrasData.name, "Mitra Name")
        Log.e(mitrasData.password, "Mitra Password")

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
                Toast.makeText(
                    requireContext(),
                    "Hello ${mitras.name}, Your Account Successfully Created!",
                    Toast.LENGTH_SHORT
                ).show()
                gotoMainActivity(newUser)
            }
        })
    }

    private fun gotoMainActivity(mitraData: Mitras) {
        val intent =
            Intent(requireActivity(), MainActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_USER, mitraData)
        startActivity(intent)
        requireActivity().finish()
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