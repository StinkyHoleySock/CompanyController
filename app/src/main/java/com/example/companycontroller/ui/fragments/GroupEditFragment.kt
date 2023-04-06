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
import com.example.companycontroller.R
import com.example.companycontroller.data.model.Group
import com.example.companycontroller.data.model.User
import com.example.companycontroller.databinding.FragmentGroupEditBinding
import com.example.companycontroller.ui.adapters.UserAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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
        if (args.addUser) {
            
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupEditBinding.inflate(inflater, container, false)
        return binding.root
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
                    val leader = document.getString("leader") ?: ""
                    binding.etGroupName.setText(document.getString("name"))
                    binding.etTask.setText(document.getString("task"))
                    binding.tvGroupLeader.text ="Руководитель группы: $leader"

                    Log.d("develop", ":)")
                } else {
                    Log.d("develop", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("develop", "get failed with ", exception)
            }


        binding.btnSave.setOnClickListener {
            val leader = "Мухаммед"
            val group = Group(
                id = args.id,
                name = binding.etGroupName.text.toString(),
                task = binding.etTask.text.toString(),
                leader = leader,
                members = mutableListOf()
            )

            db.collection("group").document(args.id)
                .set(group)
                .addOnSuccessListener {
                    Log.d("develop", "Group updated successfully")
                    Toast.makeText(requireContext(), "Успех", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.w("develop", "Error updating group", e)
                    Toast.makeText(requireContext(), "Неуспех", Toast.LENGTH_SHORT).show()
                }

        }

        binding.btnSetLeader.setOnClickListener {
            findNavController().navigate(R.id.action_groupEditFragment_to_listOfUsersDialog)
        }

        binding.tvGroupLeader.text = "Руководитель группы: "


    }
}