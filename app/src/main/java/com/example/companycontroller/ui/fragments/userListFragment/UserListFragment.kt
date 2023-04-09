package com.example.companycontroller.ui.fragments.userListFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companycontroller.R
import com.example.companycontroller.databinding.FragmentUserListBinding
import com.example.companycontroller.shared.extensions.applyVisibility
import com.example.companycontroller.ui.adapters.UserAdapter

class UserListFragment: Fragment(R.layout.fragment_user_list) {

    private var _binding: FragmentUserListBinding? = null
    private val binding: FragmentUserListBinding get() = _binding!!
    private lateinit var viewModel: UserListViewModel

    private val userAdapter by lazy {
        UserAdapter() {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Инициализация ViewBinding и ViewModel
        viewModel = ViewModelProvider(this)[UserListViewModel::class.java]
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
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

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.filterUsersByName(query)
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.filterUsersByName(newText)
                return false
            }
        })

        viewModel.userList.observe(viewLifecycleOwner) { usersList ->
            userAdapter.setData(usersList)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressCircular.applyVisibility(it)
        }
    }
}