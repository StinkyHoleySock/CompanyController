package com.example.companycontroller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companycontroller.R
import com.example.companycontroller.data.model.User
import com.example.companycontroller.databinding.FragmentUserListDialogBinding
import com.example.companycontroller.shared.extensions.applyVisibility
import com.example.companycontroller.ui.adapters.UserAdapter

class ListOfUsersDialog: DialogFragment(R.layout.fragment_user_list_dialog) {

    private var _binding: FragmentUserListDialogBinding? = null
    private val binding: FragmentUserListDialogBinding get() = _binding!!
    private lateinit var viewModel: UserListViewModel

    private val userAdapter by lazy {
        UserAdapter() {
            navigateToUser(it)
        }
    }

    private fun navigateToUser(it: User) {
        TODO("Not yet implemented")
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
            userAdapter.setData(usersList)
        }

//        viewModel.isLoading.observe(viewLifecycleOwner) {
//            binding.progressCircular.applyVisibility(it)
//        }
    }

}