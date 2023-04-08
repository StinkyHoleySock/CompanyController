package com.example.companycontroller.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.companycontroller.R
import com.example.companycontroller.data.*
import com.example.companycontroller.data.enum.UsersRole
import com.example.companycontroller.data.model.User
import com.example.companycontroller.databinding.FragmentNavigationBinding
import com.example.companycontroller.shared.extensions.applyVisibility
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging


class NavigationFragment : Fragment(R.layout.fragment_navigation) {

    private var _binding: FragmentNavigationBinding? = null
    private val binding: FragmentNavigationBinding get() = _binding!!

    lateinit var userRole: UsersRole

    override fun onCreateView(
        inflater: LayoutInflater,                    // обработка полученного юзера
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNavigationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val usersCollectionRef = db.collection("user")

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                // сохраняем токен на сервере
                Log.d("develop", "token: $token")
            }
        }

        currentUser?.uid?.let { uid ->
            usersCollectionRef.document(uid).get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    if (user?.superUser == true) {
                        binding.btnGroupsList.applyVisibility(true)
                        binding.btnMyGroup.applyVisibility(false)
                        userRole = UsersRole.SUPERUSER
                    }
                    if (user?.leader == true) {
                        binding.btnGroupsList.applyVisibility(false)
                        userRole = UsersRole.LEADER
                    }
                }
            }
        }

        binding.btnGroupsList.setOnClickListener {
            findNavController().navigate(R.id.action_navigationFragment_to_groupListFragment)
        }

        binding.btnUserList.setOnClickListener {
            findNavController().navigate(R.id.action_navigationFragment_to_userListFragment)
        }

        binding.btnMyGroup.setOnClickListener {
            val groupsCollection = db.collection("group")
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId != null) {
                groupsCollection.whereArrayContains("members", userId)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            // Здесь document - это документ группы, в которой содержится ваш id в поле members
                            val action =
                                NavigationFragmentDirections.actionNavigationFragmentToGroupDetailsFragment(
                                    document.id
                                )
                            findNavController().navigate(action)
                            // Дальнейшая обработка группы
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Обработка ошибки
                    }
            }
        }
    }
}