package com.ibunda.ilifeapps.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentOtpBinding
import com.ibunda.ilifeapps.ui.dashboard.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.TimeUnit


@ExperimentalCoroutinesApi
class OtpFragment : Fragment() {

    private lateinit var binding: FragmentOtpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var credential: PhoneAuthCredential

    private var phoneNumber: String? = null
    private var storedVerificationId: String? = ""

    private val loginViewModel: LoginViewModel by activityViewModels()

    companion object {
        const val EXTRA_PHONE_NUMBER = "extra_phone_number"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        if (arguments != null) {
            phoneNumber = requireArguments().getString(EXTRA_PHONE_NUMBER)!!
        }
        auth = Firebase.auth
        auth.setLanguageCode("id")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initOtpCallback()
        sendOtpCode()
        startCountdown()

        binding.icBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun initView() {
        val tvPhoneNumber =
            "Masukkan kode OTP yang dikirim SMS ke nomor HP-mu yang terdaftar $phoneNumber"
        binding.tvNomorOTP.text = tvPhoneNumber

        binding.txtPinEntry.setOnPinEnteredListener { str ->
            val otp = str.toString()
            if (otp.isEmpty()) {
                binding.tvFeedbackOTP.visibility = View.VISIBLE
                binding.tvFeedbackOTP.text = getString(R.string.tv_feedback_otp)
            } else {
                verifyPhoneNumberWithCode(storedVerificationId, otp)
            }
        }
    }

    private fun initOtpCallback() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
            }
        }
        // [END phone_auth_callbacks]
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        loginViewModel.signInWithGoogleFacebook(credential)
            .observe(viewLifecycleOwner, { userData ->
                if (userData != null && userData.errorMessage == null) {
                    if (userData.isNew == true) {
                        createNewUser(userData)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Welcome back, ${userData.name}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("oldUser", userData.name.toString())
                        gotoMainActivity(userData)
                    }
                } else if (userData.errorMessage != null) {
                    Toast.makeText(requireContext(), userData.errorMessage, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun sendOtpCode() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber!!)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    private fun createNewUser(userData: Users) {
        Log.d("createdNewUser", userData.name.toString())
        loginViewModel.createdNewUser(userData).observe(viewLifecycleOwner, { newUser ->
            if (newUser.isCreated) {
                Toast.makeText(
                    requireContext(),
                    "Hello ${userData.name}, Your Account Successfully Created!",
                    Toast.LENGTH_SHORT
                ).show()
                gotoMainActivity(newUser)
            }
        })
    }

    private fun gotoMainActivity(userData: Users) {
        val intent =
            Intent(requireActivity(), MainActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_USER, userData)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun startCountdown() {

        binding.tvResendOtp.setOnClickListener {
            Toast.makeText(requireContext(), "Tunggu Hingga Waktu Selesai", Toast.LENGTH_SHORT)
                .show()
        }

        val timer = object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val countdownText = "(${millisUntilFinished / 1000})"
                binding.tvCountdownOtp.text = countdownText
            }

            override fun onFinish() {
                binding.tvResendOtp.setTextColor(Color.parseColor("#36B5FE"))
                binding.tvResendOtp.setOnClickListener {
                    resendVerificationCode(phoneNumber!!, resendToken)
                    binding.tvResendOtp.setTextColor(Color.parseColor("#9E9E9E"))
                    startCountdown()
                }
            }
        }
        timer.start()
    }
}