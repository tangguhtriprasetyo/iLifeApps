package com.ibunda.mitrailifeapps.ui.login.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.databinding.FragmentRegisterOneBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class RegisterOneFragment : Fragment() {

    private lateinit var binding: FragmentRegisterOneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIsChecked()

        binding.checkBox.setOnClickListener {
            checkIsChecked()
        }

        binding.btnNext.setOnClickListener {
            if (validateName() && validateEmail() && validatePassword() && binding.checkBox.isChecked) {
                nextRegister()
            }
        }

        binding.tvPrivasi.setOnClickListener {
            val url = "https://pages.flycricket.io/ilifeapps/privacy.html"
            val uriUrl = Uri.parse(url)
            val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
            startActivity(launchBrowser)
        }

        binding.tvLogin.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

    }

    private fun checkIsChecked() {
        binding.btnNext.isEnabled = binding.checkBox.isChecked
    }

    private fun nextRegister() {

        val userName = binding.inputUsername.text.toString().trim()
        val email = binding.inputEmail.text.toString().trim()
        val password = binding.inputPassword.text.toString().trim()
        val confirmPassword = binding.inputConfirmPassword.text.toString().trim()

        val mitras = Mitras(name = userName,
            email = email,
            password = password,
            confirmPassword = confirmPassword)

        val mRegisterTwoFragment = RegisterTwoFragment()
        val mBundle = Bundle()
        mBundle.putParcelable(RegisterTwoFragment.EXTRA_USER, mitras)
        mRegisterTwoFragment.arguments = mBundle

        val mFragmentManager = parentFragmentManager
        mFragmentManager.commit {
            addToBackStack(null)
            replace(
                R.id.host_login_activity,
                mRegisterTwoFragment
            )
        }

    }

    private fun validateName(): Boolean {

        val userName = binding.inputUsername.text.toString().trim()

        return when {
            userName.length < 6 -> {
                binding.inputUsername.error = "Minimal Nama Pengguna adalah 6 huruf."
                false
            }
            userName.length > 20 -> {
                binding.inputUsername.error = "Maksimal Nama Pengguna adalah 20 huruf."
                false
            }
            userName.contains(" ") -> {
                binding.inputUsername.error = "Nama Pengguna tidak boleh menggunakan spasi."
                false
            }
            else -> {
                true
            }
        }

    }

    private fun validateEmail(): Boolean {

        val email = binding.inputEmail.text.toString().trim()

        return when {
            !email.contains("@") && !email.contains(".") -> {
                binding.inputEmail.error = "Masukkan Email yang Valid."
                false
            }
            !email.contains("@") -> {
                binding.inputEmail.error = "Masukkan Email yang Valid."
                false
            }
            !email.contains(".") -> {
                binding.inputEmail.error = "Masukkan Email yang Valid."
                false
            }
            email.contains(" ") -> {
                binding.inputEmail.error = "Masukkan Email yang Valid."
                false
            }
            email.length < 6 -> {
                binding.inputEmail.error = "Masukkan Email yang Valid."
                false
            }
            else -> {
                true
            }
        }
    }

    private fun validatePassword(): Boolean {

        val password = binding.inputPassword.text.toString().trim()
        val confirmPassword = binding.inputConfirmPassword.text.toString().trim()

        return when {
            password.length < 6 -> {
                binding.inputPassword.error = "Minimal Kata Sandi adalah 6 huruf."
                false
            }
            password != confirmPassword -> {
                Toast.makeText(requireContext(), "Kata Sandi tidak sama.", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

}