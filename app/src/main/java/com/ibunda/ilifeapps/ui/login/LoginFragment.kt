package com.ibunda.ilifeapps.ui.login

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentLoginBinding
import com.ibunda.ilifeapps.ui.dashboard.MainActivity

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val loginViewModel: LoginViewModel by activityViewModels()

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


        initGoogleSignInClient()

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)!!
                        Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                        loginViewModel.signInWithGoogle(account.idToken!!)
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
            resultLauncher.launch(signInIntent)
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
}
