package com.example.companycontroller.ui.fragments.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companycontroller.R
import com.example.companycontroller.databinding.FragmentMessagesBinding
import com.example.companycontroller.ui.adapters.MessageAdapter

class MessageFragment : Fragment(R.layout.fragment_messages) {

    private var _binding: FragmentMessagesBinding? = null
    private val binding: FragmentMessagesBinding get() = _binding!!
    private lateinit var viewModel: MessageViewModel

    private lateinit var messageAdapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Инициализация ViewBinding и ViewModel
        viewModel = ViewModelProvider(this)[MessageViewModel::class.java]
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageAdapter = MessageAdapter()

        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            messageAdapter.setData(messages)
        }

        //Установка данных в список
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = messageAdapter
        }
    }
}