package com.example.companycontroller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.companycontroller.R
import com.example.companycontroller.databinding.FragmentGroupBinding

class GroupListFragment: Fragment(R.layout.fragment_group) {

    private var _binding: FragmentGroupBinding? = null
    private val binding: FragmentGroupBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}