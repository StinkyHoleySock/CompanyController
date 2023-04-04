package com.example.companycontroller.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companycontroller.R
import com.example.companycontroller.data.model.User
import com.example.companycontroller.databinding.FragmentUserListBinding
import com.example.companycontroller.ui.adapters.UserAdapter

class UserListFragment: Fragment(R.layout.fragment_user_list) {

    private var _binding: FragmentUserListBinding? = null
    private val binding: FragmentUserListBinding get() = _binding!!
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

        viewModel.userList.observe(viewLifecycleOwner) { usersList ->
            userAdapter.setData(usersList)
        }
    }

    private fun initSearchView() {
//        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                val query = getFilter(binding.radioGroup.checkedRadioButtonId)+query
//                Log.d("develop", "queryts: $query")
//                viewModel.getBooksList(query)
//                return false
//            }
//            override fun onQueryTextChange(newText: String): Boolean {
//                val query = getFilter(binding.radioGroup.checkedRadioButtonId)+newText
//                Log.d("develop", "querytc: $query")
//                viewModel.getBooksList(query)
//                return false
//            }
//        })
    }
}