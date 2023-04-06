package com.example.companycontroller.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.companycontroller.R
import com.example.companycontroller.data.model.Group
import com.example.companycontroller.databinding.FragmentCreateGroupBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class GroupCreateFragment : Fragment(R.layout.fragment_create_group) {

    private var _binding: FragmentCreateGroupBinding? = null
    private val binding: FragmentCreateGroupBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val id = UUID.randomUUID().toString()
            val group = Group(
                id = id,
                name = binding.etGroupName.text.toString(),
                task = binding.etTask.text.toString(),
                leader = null,
                members = mutableListOf()
            )

            if (binding.etGroupName.text.toString().isNotEmpty()) {

                val docRef = db.collection("group").document(id)
                docRef.set(group)
                    .addOnSuccessListener { Log.d("develop", "Group saved successfully") }
                    .addOnFailureListener { e -> Log.e("develop", "Error saving group", e) }

            }
        }
    }
}