package com.example.companycontroller.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companycontroller.R
import com.example.companycontroller.data.model.Group
import com.example.companycontroller.data.model.User
import com.example.companycontroller.databinding.FragmentGroupEditBinding
import com.example.companycontroller.ui.adapters.UserAdapter
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GroupEditFragment : Fragment(R.layout.fragment_group_edit) {

    private var _binding: FragmentGroupEditBinding? = null
    private val binding: FragmentGroupEditBinding get() = _binding!!
    private val userAdapter by lazy {
        UserAdapter() {
            userAction(it)
        }
    }

    private val args: GroupEditFragmentArgs by navArgs()

    private fun userAction(user: User) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    private suspend fun getUsersByIds(userIds: List<String>): List<User> {
        val db = Firebase.firestore

        val users = mutableListOf<User>()
        val collectionRef = db.collection("user")

        userIds.forEach { userId ->
            collectionRef.document(userId).get().await()?.toObject(User::class.java)?.let { user ->
                users.add(user)
            }
        }

        return users
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        //Просмотр группы: загрузка полей по id документа
        db.collection("group")
            .document(args.id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val group = document.toObject(Group::class.java)
                    val members = group?.members ?: listOf()

                    binding.etGroupName.setText(group?.name)
                    binding.etTask.setText(group?.task)

                    CoroutineScope(Dispatchers.Main).launch {
                        // Обработка результата
                        val leaderUser = getUsersByIds(listOf(group?.leader) as List<String>)
                        val leader =
                            if (leaderUser.isNotEmpty()) "${leaderUser[0].surname} ${leaderUser[0].name}" else ""
                        binding.tvGroupLeader.text = "Руководитель группы: $leader"
                    }


                    CoroutineScope(Dispatchers.Main).launch {
                        // Обработка результата
                        val users = getUsersByIds(members)
                        Log.d("develop", "users: ${users}")
                        userAdapter.setData(users)
                    }

                    binding.rvUsersInGroup.apply {
                        layoutManager = LinearLayoutManager(
                            context,
                            LinearLayoutManager.VERTICAL, false
                        )
                        adapter = userAdapter
                    }

                } else {
                    Log.d("develop", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("develop", "get failed with ", exception)
            }

        binding.btnSave.setOnClickListener {
            val groupRef = db.collection("group").document(args.id)
            groupRef.update("name", (binding.etGroupName.text.toString()))
            groupRef.update("task", (binding.etTask.text.toString()))
        }

        binding.btnSetLeader.setOnClickListener {
            val action = GroupEditFragmentDirections.actionGroupEditFragmentToListOfUsersDialog(
                args.id,
                true
            )
            findNavController().navigate(action)
        }

        binding.btnAdd.setOnClickListener {
            val action = GroupEditFragmentDirections.actionGroupEditFragmentToListOfUsersDialog(
                args.id,
                false
            )
            findNavController().navigate(action)
        }
    }
}