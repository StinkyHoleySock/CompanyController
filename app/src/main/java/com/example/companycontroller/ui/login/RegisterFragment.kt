package com.example.companycontroller.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.companycontroller.R
import com.example.companycontroller.databinding.FragmentRegisterBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            tvBtnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }

            btnRegister.setOnClickListener {

                when {

                    etName.text.toString().isEmpty() -> toast(tilName)

                    etSurname.text.toString().isEmpty() -> toast(tilSurname)

                    etEmail.text.toString().isEmpty() -> toast(tilEmail)

                    etPassword.text.toString().isEmpty() -> toast(tilPassword)

                    // TODO: accept rules

                    else -> firebaseSignUp()
                }

            }
        }
    }

    private fun firebaseSignUp() {
        auth.createUserWithEmailAndPassword(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val user = FirebaseAuth.getInstance().currentUser

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(
                        binding.etName.text.toString() + ' ' + binding.etSurname.text.toString()
                    ).build()

                user!!.updateProfile(profileUpdates)

                findNavController().navigate(R.id.action_registerFragment_to_mainFragment)

            } else {
                Toast.makeText(
                    activity, "Authentication failed. ${task.exception}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    private fun toast(field: TextInputLayout) {
        Toast.makeText(
            activity,
            "Поле '${field.hint.toString()}' не может быть пустым!",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun validateUser(
        username: String,
        surname: String,
        email: String,
        password: String
    ) : Boolean {

        return (username.isNotEmpty() && surname.isNotEmpty()
                && email.isNotEmpty() && password.isNotEmpty())
    }

    fun checkRules(isChecked: Boolean): String {
        return  if (isChecked) "Rules is accepted"
        else "Rules not accepted"
    }

    fun validatePassword(password: String) : Boolean {
        return (password.length >= 8)
    }

}