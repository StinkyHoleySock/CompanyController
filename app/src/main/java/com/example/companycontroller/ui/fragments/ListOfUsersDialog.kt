package com.example.companycontroller.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companycontroller.R
import com.example.companycontroller.data.model.User
import com.example.companycontroller.databinding.FragmentUserListDialogBinding
import com.example.companycontroller.ui.adapters.UserAdapter
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListOfUsersDialog : Fragment(R.layout.fragment_user_list_dialog) {

    private var _binding: FragmentUserListDialogBinding? = null
    private val binding: FragmentUserListDialogBinding get() = _binding!!
    private lateinit var viewModel: UserListViewModel

    private val args: ListOfUsersDialogArgs by navArgs()


    private val userAdapter by lazy {
        UserAdapter() {
            updateUsers(it)
        }
    }

    private fun updateUsers(user: User) {
        val db = Firebase.firestore
        val groupRef = db.collection("group").document(args.id)

        if (args.setLeader) {
            groupRef.update("leader", (user.id))
        } else {
            groupRef.update("members", FieldValue.arrayUnion(user.id))
        }
//        findNavController().navigate(R.id.action_listOfUsersDialog_to_groupEditFragment2)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[UserListViewModel::class.java]
        _binding = FragmentUserListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = userAdapter
        }

        viewModel.userList.observe(viewLifecycleOwner) { usersList ->
            Log.d("develop", "obs_louf ${usersList}")
            userAdapter.setData(usersList)
        }

//        viewModel.isLoading.observe(viewLifecycleOwner) {
//            binding.progressCircular.applyVisibility(it)
//        }
    }

}