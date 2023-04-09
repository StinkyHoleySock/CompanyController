package com.example.companycontroller.ui.fragments.editGroupFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companycontroller.R
import com.example.companycontroller.data.model.Group
import com.example.companycontroller.data.model.User
import com.example.companycontroller.databinding.FragmentGroupEditBinding
import com.example.companycontroller.shared.extensions.ToastError
import com.example.companycontroller.ui.adapters.UserAdapter
import com.example.companycontroller.ui.fragments.userListFragment.UserListViewModel
import com.example.companycontroller.ui.fragments.usersDialog.GroupListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class GroupEditFragment : Fragment(R.layout.fragment_group_edit) {

    //Объявление ViewBinding и ViewModel
    private var _binding: FragmentGroupEditBinding? = null
    private val binding: FragmentGroupEditBinding get() = _binding!!
    private lateinit var viewModel: GroupListViewModel

    //Инициализация адаптера для юзеров
    private val userAdapter by lazy {
        UserAdapter() {
            userAction(it)
        }
    }

    //Объявление аргументов из навигации
    private val args: GroupEditFragmentArgs by navArgs()

    //Метод для обработки нажатия на пользователя
    private fun userAction(user: User) {
        val db = Firebase.firestore
        val groupRef = db.collection("group").document(args.id)
        Log.d("develop", "click")

        //Удаление пользователя из списка
        groupRef.update("members", FieldValue.arrayRemove(user.id))
        viewModel.deleteUser(user)

        //Обновление списка
        db.collection("group")
            .document(args.id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val group = document.toObject(Group::class.java)
                    val members = group?.members ?: listOf()
                    viewModel.getUsersList(members)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Инициализация ViewBinding и ViewModel
        viewModel = ViewModelProvider(this)[GroupListViewModel::class.java]
        _binding = FragmentGroupEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Метод для получения списка пользователей, зная их ID
    private suspend fun getUsersByIds(userIds: List<String>): MutableList<User> {
        val db = Firebase.firestore

        val users = mutableListOf<User>()
        val collectionRef = db.collection("user")

        userIds.forEach { userId ->
            collectionRef.document(userId).get().await()?.toObject(User::class.java)?.let { user ->
                users.add(user)
            }
        }
        return users
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore

        //Обработка кнопки отмены
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        //Подписка на изменение списка пользователей
        viewModel.userList.observe(viewLifecycleOwner) {
            userAdapter.setData(it)
            Log.d("develop", "obs")
        }

        //Просмотр группы: загрузка полей по id документа
        db.collection("group")
            .document(args.id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val group = document.toObject(Group::class.java)
                    val members = group?.members ?: listOf()

                    binding.etGroupName.setText(group?.name)
                    binding.etTask.setText(group?.task)

                    //Получение начальника группы
                    CoroutineScope(Dispatchers.Main).launch {
                        // Обработка результата
                        val leaderUser = getUsersByIds(listOf(group?.leader) as List<String>)
                        val leader = if (leaderUser.isNotEmpty()) "${leaderUser[0].surname} ${leaderUser[0].name}" else ""
                        binding.tvGroupLeader.text = "Руководитель группы: $leader"
                    }

                    //Получение работников в группе
                    CoroutineScope(Dispatchers.Main).launch {
                        val users = getUsersByIds(members)
                        viewModel.setUsers(users)
                    }

                    binding.rvUsersInGroup.apply {
                        layoutManager = LinearLayoutManager(
                            context,
                            LinearLayoutManager.VERTICAL, false
                        )
                        adapter = userAdapter
                    }

                } else {
                    Log.d("develop", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("develop", "get failed with ", exception)
            }

        //Обработка кнопки сохранить
        binding.btnSave.setOnClickListener {
            when {
                //Валидация полей
                binding.etGroupName.text.toString().isEmpty() -> ToastError.toast(
                    requireActivity(),
                    binding.tilGroupName
                )
                binding.etTask.text.toString().isEmpty() -> ToastError.toast(
                    requireActivity(),
                    binding.tilTask
                )

                //Сохранение изменений и навигация назад при услоии, что поля валидны
                else -> {
                    val groupRef = db.collection("group").document(args.id)
                    groupRef.update("name", (binding.etGroupName.text.toString()))
                    groupRef.update("task", (binding.etTask.text.toString()))

                    findNavController().popBackStack()
                }
            }
        }

        //Обработка кнопки назначить руководителя
        binding.btnSetLeader.setOnClickListener {
            val action = GroupEditFragmentDirections.actionGroupEditFragmentToListOfUsersDialog(
                args.id,
                true,
                false
            )
            findNavController().navigate(action)
        }

        //Обработка нажатия на добавление нового работника
        binding.btnAdd.setOnClickListener {
            val action = GroupEditFragmentDirections.actionGroupEditFragmentToListOfUsersDialog(
                args.id,
                false,
                true
            )
            //Навигация на экрас с выбором работника
            findNavController().navigate(action)
        }
    }
}