package com.example.companycontroller.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.companycontroller.R
import com.example.companycontroller.data.enum.UsersRole
import com.example.companycontroller.data.model.User
import com.example.companycontroller.databinding.FragmentNavigationBinding
import com.example.companycontroller.shared.extensions.applyVisibility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class NavigationFragment : Fragment(R.layout.fragment_navigation) {

    private var _binding: FragmentNavigationBinding? = null
    private val binding: FragmentNavigationBinding get() = _binding!!

    private lateinit var userRole: UsersRole
    private lateinit var userName: String

    override fun onCreateView(
        inflater: LayoutInflater,
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

        //Установка роли текущего пользователя
        currentUser?.uid?.let { uid ->
            usersCollectionRef.document(uid).get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {

                    val user = documentSnapshot.toObject(User::class.java)
                    if (user?.superUser == true) {
                        binding.btnGroupsList.applyVisibility(true)
                        binding.btnMyGroup.applyVisibility(false)
                        binding.btnMyMessages.applyVisibility(true)
                        userRole = UsersRole.SUPERUSER
                    }
                    if (user?.leader == true) {
                        binding.btnGroupsList.applyVisibility(false)
                        userRole = UsersRole.LEADER
                    }

                    userName = "${user?.surname} ${user?.name}"
                }
            }
        }

        //Навигация на список групп
        binding.btnGroupsList.setOnClickListener {
            findNavController().navigate(R.id.action_navigationFragment_to_groupListFragment)
        }

        //Навигация на список работников
        binding.btnUserList.setOnClickListener {
            findNavController().navigate(R.id.action_navigationFragment_to_userListFragment)
        }

        //Навигация на список сообщений
        binding.btnMyMessages.setOnClickListener {
            findNavController().navigate(R.id.action_navigationFragment_to_messageFragment)
        }

        //Навигация на мою группу
        binding.btnMyGroup.setOnClickListener {
            val groupsCollection = db.collection("group")
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            //Если пользователь существует, то происходит навигация на фрагмент с группой
            if (userId != null) {
                groupsCollection.whereArrayContains("members", userId)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            // Здесь document - это документ группы, в которой содержится id в поле members
                            val action =
                                NavigationFragmentDirections.actionNavigationFragmentToGroupDetailsFragment(
                                    document.id, userName, userId
                                )
                            findNavController().navigate(action)
                        }
                    }
            }
        }
    }
}