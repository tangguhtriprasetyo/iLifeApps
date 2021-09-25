package com.ibunda.ilifeapps.ui.login

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentLoginBinding
import com.ibunda.ilifeapps.ui.dashboard.MainActivity
import com.ibunda.ilifeapps.utils.DatePickerHelper
import com.ibunda.ilifeapps.utils.TimePickerHelper
import java.util.*

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private val loginViewModel: LoginViewModel by activityViewModels()

    private var phoneNumber: String? = null

    lateinit var datePicker: DatePickerHelper
    lateinit var timePicker: TimePickerHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIsChecked()
        datePicker = DatePickerHelper(requireContext())
        timePicker = TimePickerHelper(requireContext(), true)

        callbackManager = CallbackManager.Factory.create()
        initGoogleSignInClient()
        initFacebookClient()

        val loginGoogleLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)!!
                        Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                        val googleCredential =
                            GoogleAuthProvider.getCredential(account.idToken!!, null)
                        loginViewModel.signInWithGoogleFacebook(googleCredential)
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
                                    Toast.makeText(
                                        requireContext(),
                                        userData.errorMessage,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })
                    } catch (e: ApiException) {
                        // Google Sign In failed, update UI appropriately
                        Toast.makeText(requireContext(), "Login Failed, $e", Toast.LENGTH_LONG)
                            .show()
                        Log.w(ContentValues.TAG, "Google sign in failed", e)
                    }
                }
            }
        initOnCLick(loginGoogleLauncher)
    }

    private fun initOnCLick(loginGoogleLauncher: ActivityResultLauncher<Intent>) {

        binding.gSignIn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            loginGoogleLauncher.launch(signInIntent)
        }

        binding.btnMulai.setOnClickListener {

            phoneNumber = binding.etPhoneNumber.text.toString()
            if (verifyPhoneNumberFormat()) {
                gotoOtpFragment()
            } else {
                Toast.makeText(requireContext(), "Masukkan Nomor Telepon Valid!", Toast.LENGTH_LONG)
                    .show()
            }

        }

        binding.checkBox.setOnClickListener {
            checkIsChecked()
        }
    }

    private fun checkIsChecked() {
        if (binding.checkBox.isChecked) {
            binding.btnMulai.isEnabled = true
            binding.gSignIn.isEnabled = true
            binding.loginButton.isEnabled = true
            binding.gSignIn.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
        } else {
            binding.btnMulai.isEnabled = false
            binding.gSignIn.isEnabled = false
            binding.loginButton.isEnabled = false
            binding.gSignIn.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.common_google_signin_btn_text_light_disabled
                )
            )
        }
    }

    private fun verifyPhoneNumberFormat(): Boolean {

        var isVerified = false
        val checkFormat = phoneNumber!!.substring(0, 3)
        Log.d(
            TAG,
            "verifyPhoneNumberFormat: $checkFormat | ${phoneNumber!!.first()} | ${phoneNumber!!.length}"
        )
        if (phoneNumber != null && phoneNumber!!.length >= 8) {

            Log.d(TAG, "verifyLength: ${phoneNumber!!.length}")

            if (phoneNumber!!.first().toString() == "0" || checkFormat == "+62") {

                Log.d(TAG, "verifyFirsCheckFormat: ${phoneNumber!!.first()} | $checkFormat}")

                if (phoneNumber!!.first().toString() == "0") {

                    Log.d(TAG, "verifyFirst: ${phoneNumber!!.first()}")
                    phoneNumber = phoneNumber!!.replace("0", "+62", ignoreCase = true)
                }
                isVerified = true
            }
        } else {
            isVerified = false
        }
        return isVerified
    }

    private fun gotoOtpFragment() {

        val mFragmentManager = parentFragmentManager
        val mOtpFragment = OtpFragment()

        val mBundle = Bundle()
        mBundle.putString(OtpFragment.EXTRA_PHONE_NUMBER, phoneNumber)
        mOtpFragment.arguments = mBundle

        mFragmentManager.commit {
            addToBackStack(null)
            replace(
                R.id.host_login_activity,
                mOtpFragment,
                LoginActivity::class.java.simpleName
            )
        }
    }

    private fun createNewUser(userData: Users) {
        Log.d("createdNewUser", userData.name.toString())
        loginViewModel.createdNewUser(userData).observe(viewLifecycleOwner, { newUser ->
            if (newUser.isCreated == true) {
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

    private fun initGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun initFacebookClient() {

        Log.d(TAG, "initFacebook")

        binding.loginButton.setReadPermissions("email", "public_profile")
        binding.loginButton.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                Log.d("FacebookTOKEN", loginResult.accessToken.toString())
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
            }
        })

    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")
        val credential = FacebookAuthProvider.getCredential(token.token)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Pass the activity result back to the Facebook SDK
        Log.d(TAG, "facebookActivityResult")
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
