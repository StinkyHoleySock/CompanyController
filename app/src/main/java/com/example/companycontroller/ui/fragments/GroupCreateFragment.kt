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
            val group = Group(
                id = UUID.randomUUID().toString(),
                name = binding.etGroupName.text.toString(),
                task = binding.etTask.text.toString(),
                members = mutableListOf()
            )

            if (binding.etGroupName.text.toString().isNotEmpty()) {
                val groupCollection = db.collection("group")

                groupCollection.add(group)
                    .addOnSuccessListener { documentReference ->
                        Log.d("develop", "DocumentSnapshot written with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("develop", "Error adding document", e)
                    }
            }
        }
    }
}