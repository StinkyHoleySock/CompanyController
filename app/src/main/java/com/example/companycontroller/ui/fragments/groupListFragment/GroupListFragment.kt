package com.example.companycontroller.ui.fragments.groupListFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companycontroller.R
import com.example.companycontroller.data.model.Group
import com.example.companycontroller.databinding.FragmentGroupBinding
import com.example.companycontroller.shared.extensions.applyVisibility
import com.example.companycontroller.ui.adapters.GroupAdapter

class GroupListFragment: Fragment(R.layout.fragment_group) {

    private var _binding: FragmentGroupBinding? = null
    private val binding: FragmentGroupBinding get() = _binding!!
    private lateinit var viewModel: GroupListViewModel

    private val groupAdapter by lazy {
        GroupAdapter() {
            navigateToGroupEditFragment(it)
        }
    }

    private fun navigateToGroupEditFragment(it: Group) {
        val action =
            GroupListFragmentDirections.actionGroupListFragmentToGroupEditFragment(it.id, true)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Инициализация ViewBinding и ViewModel
        viewModel = ViewModelProvider(this)[GroupListViewModel::class.java]
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvGroups.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = groupAdapter
        }

        binding.addGroup.setOnClickListener {
            findNavController().navigate(R.id.action_groupListFragment_to_groupCreateFragment)
        }

        //Обработка поиска
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            //Когда текст напечатан
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.filterGroupsByName(query)
                return false
            }
            //Когда текст изменился
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.filterGroupsByName(newText)
                return false
            }
        })

        //Подписка на отфильтрованный список
        viewModel.groupList.observe(viewLifecycleOwner) { groupsList ->
            groupAdapter.setData(groupsList)
        }

        //Подписка на индикатор загрузки
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressCircular.applyVisibility(it)
        }
    }

    //ПОлучение групп после навигаций
    override fun onResume() {
        super.onResume()
        viewModel.getGroups()
    }
}