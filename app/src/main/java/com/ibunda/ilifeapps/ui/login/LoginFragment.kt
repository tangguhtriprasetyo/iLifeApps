package com.ibunda.ilifeapps.ui.login

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
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
import com.ibunda.ilifeapps.databinding.FragmentDialogKategoriMitraBinding
import com.ibunda.ilifeapps.databinding.FragmentLoginBinding
import com.ibunda.ilifeapps.ui.dashboard.MainActivity
import com.ibunda.ilifeapps.utils.DatePickerHelper
import com.ibunda.ilifeapps.utils.TimePickerHelper
import java.text.SimpleDateFormat
import java.util.*

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private val loginViewModel: LoginViewModel by activityViewModels()

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

        datePicker = DatePickerHelper(requireContext())
        timePicker = TimePickerHelper(requireContext(), true)
        //testDialog
        binding.tv.setOnClickListener {
            showDialog()
//            showDatePickerDialog()
//            showTimePickerDialog()
        }

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
                                if (userData != null) {
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

        binding.gSignIn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            loginGoogleLauncher.launch(signInIntent)
        }
    }

    //testDialogBuilder
    private fun showDialog() {
        val dialog = Dialog(requireContext())
        val inflater = this.layoutInflater
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        val binding :FragmentDialogKategoriMitraBinding = FragmentDialogKategoriMitraBinding.inflate(inflater)
        dialog.setContentView(binding.root)

        binding.btnPilihKategori.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }


        dialog.show()
    }

    //testDialog
    private fun showTimePickerDialog() {
        val cal = Calendar.getInstance()
        val h = cal.get(Calendar.HOUR_OF_DAY)
        val m = cal.get(Calendar.MINUTE)
        timePicker.showDialog(h, m, object : TimePickerHelper.Callback {
            override fun onTimeSelected(hourOfDay: Int, minute: Int) {
                val hourStr = if (hourOfDay < 10) "0${hourOfDay}" else "${hourOfDay}"
                val minuteStr = if (minute < 10) "0${minute}" else "${minute}"
                binding.tv.text = "${hourOfDay}:${minuteStr}"
            }
        })
    }

    //testDialog
    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)

        datePicker.setMinDate(cal.timeInMillis)
        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(datePicker: View, dayofMonth: Int, month: Int, year: Int) {

                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                cal.set(dayofMonth, month, year)
                Log.d(dayofMonth.toString() + month.toString() + year.toString(), "resultDate")
                val date = dateFormat.format(cal.time)
                binding.tv.text = date
            }
        })
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
                if (userData != null) {
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
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Pass the activity result back to the Facebook SDK
        Log.d(TAG, "facebookActivityResult")
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
