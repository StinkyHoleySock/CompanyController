package com.example.companycontroller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.companycontroller.R
import com.example.companycontroller.databinding.FragmentNavigationBinding

class NavigationFragment: Fragment(R.layout.fragment_navigation) {

    private var _binding: FragmentNavigationBinding? = null
    private val binding: FragmentNavigationBinding get() = _binding!!

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

        binding.btnGroupsList.setOnClickListener {
            findNavController().navigate(R.id.action_navigationFragment_to_groupListFragment)
        }

        binding.btnUserList.setOnClickListener {
            findNavController().navigate(R.id.action_navigationFragment_to_userListFragment)
        }

        binding.btnMyGroup.setOnClickListener {

        }
    }
}