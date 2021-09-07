package com.ibunda.mitrailifeapps.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.databinding.FragmentLoginBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainActivity
import com.ibunda.mitrailifeapps.ui.login.register.RegisterOneFragment
import com.ibunda.mitrailifeapps.utils.ProgressDialogHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            if (validateEmail() && validatePassword()) {
                userLogin()
            }
        }

        binding.tvRegister.setOnClickListener {
            val mRegisterOneFragment = RegisterOneFragment()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.commit {
                addToBackStack(null)
                replace(
                    R.id.host_login_activity,
                    mRegisterOneFragment
                )
            }
        }

    }

    private fun userLogin() {

        progressDialog(true)

        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        loginViewModel.signInWithEmail(email, password)
            .observe(viewLifecycleOwner, { mitraData ->
                if (mitraData != null && mitraData.errorMessage == null) {
                    // Login sukses, masuk ke Main Activity
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user!!.isEmailVerified) {
                        progressDialog(false)
                        gotoMainActivity(mitraData)
                    } else {
                        progressDialog(false)
                        FirebaseAuth.getInstance().signOut()
                        Toast.makeText(requireContext(), "Silahkan verifikasi email anda.", Toast.LENGTH_SHORT).show()
                        //restart this activity
                    }
                } else if (mitraData.errorMessage != null) {
                    progressDialog(false)
                    Toast.makeText(
                        requireContext(),
                        mitraData.errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
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

    private fun validateEmail(): Boolean {

        val email = binding.etEmail.text.toString().trim()

        return when {
            !email.contains("@") && !email.contains(".") -> {
                binding.etEmail.error = "Masukkan Email yang Valid."
                false
            }
            !email.contains("@") -> {
                binding.etEmail.error = "Masukkan Email yang Valid."
                false
            }
            !email.contains(".") -> {
                binding.etEmail.error = "Masukkan Email yang Valid."
                false
            }
            email.contains(" ") -> {
                binding.etEmail.error = "Masukkan Email yang Valid."
                false
            }
            email.length < 6 -> {
                binding.etEmail.error = "Masukkan Email yang Valid."
                false
            }
            else -> {
                true
            }
        }

    }

    private fun validatePassword(): Boolean {

        val password = binding.etPassword.text.toString().trim()

        return when {
            password.length < 6 -> {
                binding.etPassword.error = "Masukkan Kata Sandi dengan benar"
                false
            }
            else -> {
                true
            }
        }
    }

    private fun progressDialog(state: Boolean) {
        val dialog = ProgressDialogHelper.setProgressDialog(requireActivity(), "Loading...")
        if (state) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }
}