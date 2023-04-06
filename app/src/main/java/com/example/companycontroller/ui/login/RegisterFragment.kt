package com.example.companycontroller.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.companycontroller.R
import com.example.companycontroller.data.model.User
import com.example.companycontroller.databinding.FragmentRegisterBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Инициализация binding
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            //Навигация на экран входа
            tvBtnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }

            btnRegister.setOnClickListener {
                when {
                    //Валидация полей
                    etName.text.toString().isEmpty() -> toast(tilName)
                    etSurname.text.toString().isEmpty() -> toast(tilSurname)
                    etPatronymic.text.toString().isEmpty() -> toast(tilPatronymic)
                    etEmail.text.toString().isEmpty() -> toast(tilEmail)
                    etPassword.text.toString().isEmpty() -> toast(tilPassword)

                    //Регистрация
                    else -> firebaseSignUp(
                        name = etName.text.toString(),
                        surname = etSurname.text.toString(),
                        patronymic = etPatronymic.text.toString(),
                        email = etEmail.text.toString(),
                        password = etPassword.text.toString()
                    )
                }

            }
        }
    }

    //Метод для регистрации пользователя
    private fun firebaseSignUp(
        name: String,
        surname: String,
        patronymic: String,
        email: String,
        password: String
    ) {

        val db = FirebaseFirestore.getInstance()
        val user = User(
            id = UUID.randomUUID().toString(),
            name = name,
            surname = surname,
            patronymic = patronymic,
            email = email,
            isLeader = false,
            isSuperUser = !binding.acceptSwitch.isChecked
        )
        val userCollection = db.collection("user")

        userCollection.add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("develop", "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("develop", "Error adding document", e)
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
    ): Boolean {

        return (username.isNotEmpty() && surname.isNotEmpty()
                && email.isNotEmpty() && password.isNotEmpty())
    }

    fun checkRules(isChecked: Boolean): String {
        return if (isChecked) "Rules is accepted"
        else "Rules not accepted"
    }

    fun validatePassword(password: String): Boolean {
        return (password.length >= 8)
    }

}