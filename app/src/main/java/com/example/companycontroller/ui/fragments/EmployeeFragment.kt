package com.example.companycontroller.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.companycontroller.R
import com.example.companycontroller.databinding.FragmentEmployeeBinding

class EmployeeFragment: Fragment(R.layout.fragment_employee) {

    private var _binding: FragmentEmployeeBinding? = null
    private val binding: FragmentEmployeeBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}