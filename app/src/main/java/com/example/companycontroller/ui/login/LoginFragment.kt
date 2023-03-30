package com.example.companycontroller.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.companycontroller.R
import com.example.companycontroller.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment: Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val currentUser: FirebaseUser? = auth.currentUser
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            tvRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            btnLogin.setOnClickListener {

                when {
                    etEmail.toString().isEmpty() -> {
                        Toast.makeText(activity, "$etEmail is empty!", Toast.LENGTH_SHORT)
                            .show()
                    }

                    etPassword.toString().isEmpty() -> {
                        Toast.makeText(activity, "$etPassword is empty!", Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> firebaseSignIn(etEmail.text.toString(), etPassword.text.toString())
                }

            }

        }

    }

    private fun firebaseSignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(
            email, password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                // TODO: bundleOf
                //val user = auth.currentUser
                //updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(activity, "Authentication failed. ${task.exception}",
                    Toast.LENGTH_LONG).show()
                //updateUI(null)
            }
        }
    }

    fun validateUser(email: String, password: String): Boolean {
        return (email.isNotEmpty() && password.isNotEmpty() && password.length >= 8)
    }

    fun isEmailValid(email: String): Boolean {
        return email.contains("mail", false)
    }

}