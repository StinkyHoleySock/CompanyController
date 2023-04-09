package com.example.companycontroller.ui.fragments.createGroupFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.companycontroller.R
import com.example.companycontroller.data.model.Group
import com.example.companycontroller.data.model.User
import com.example.companycontroller.databinding.FragmentCreateGroupBinding
import com.example.companycontroller.shared.extensions.ToastError.toast
import com.example.companycontroller.ui.adapters.UserSpinnerAdapter
import com.example.companycontroller.ui.fragments.userListFragment.UserListViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class GroupCreateFragment : Fragment(R.layout.fragment_create_group) {

    private var _binding: FragmentCreateGroupBinding? = null
    private val binding: FragmentCreateGroupBinding get() = _binding!!

    private lateinit var viewModel: UserListViewModel
    private var selectedGroupLeader: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Инициализация ViewBinding и ViewModel
        viewModel = ViewModelProvider(this)[UserListViewModel::class.java]
        _binding = FragmentCreateGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Инициализация объектов
        val spinner = binding.spinnerLeader
        val users = mutableListOf<User>()

        //Подписка на обновление списка пользователей
        viewModel.userList.observe(viewLifecycleOwner) {
            users.addAll(it)
            val adapter = UserSpinnerAdapter(requireContext(), users)

            //Установка данных в спиннер
            spinner.adapter = adapter

            //Обработка нажатия на спиннер
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    spinner.setSelection(position)
                    selectedGroupLeader = adapter.getItem(position)?.id
                    Log.d("develop", "sU: ${adapter.getItem(position)}")
                }

                // Обработка случая, когда ничего не выбрано
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }

        //Обработка нажатия на кнопку сохранить
        binding.btnSave.setOnClickListener {
            when {
                //Валидация полей
                binding.etGroupName.text.toString().isEmpty() -> toast(
                    requireActivity(),
                    binding.tilGroupName
                )
                binding.etTask.text.toString().isEmpty() -> toast(
                    requireActivity(),
                    binding.tilTask
                )
                //Если все поля валидны, то сохраняем
                else -> {
                    val db = FirebaseFirestore.getInstance()
                    val id = UUID.randomUUID().toString()
                    val group = Group(
                        id = id,
                        name = binding.etGroupName.text.toString(),
                        task = binding.etTask.text.toString(),
                        leader = selectedGroupLeader,
                        members = mutableListOf()
                    )

                    val docRef = db.collection("group").document(id)
                    docRef.set(group) //Хдесь происходит сохранение группы в БД
                        .addOnSuccessListener {
                            findNavController().popBackStack()
                            Log.d("develop", "Group saved successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.e("develop", "Error saving group", e)
                        }
                }
            }
        }

        //При нажатии кнопки отмена, навигация на предыдущий экран
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}