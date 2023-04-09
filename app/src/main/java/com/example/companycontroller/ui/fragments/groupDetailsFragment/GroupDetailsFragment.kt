package com.example.companycontroller.ui.fragments.groupDetailsFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companycontroller.FcmNotificationsSender
import com.example.companycontroller.R
import com.example.companycontroller.data.model.Group
import com.example.companycontroller.data.model.Message
import com.example.companycontroller.data.model.User
import com.example.companycontroller.databinding.FragmentGroupDetailsBinding
import com.example.companycontroller.shared.constants.Constants
import com.example.companycontroller.ui.adapters.UserAdapter
import com.example.companycontroller.ui.fragments.usersDialog.GroupListViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class GroupDetailsFragment : Fragment(R.layout.fragment_group_details) {

    //Объявление ViewBinding и ViewModel
    private var _binding: FragmentGroupDetailsBinding? = null
    private val binding: FragmentGroupDetailsBinding get() = _binding!!
    private lateinit var viewModel: GroupListViewModel

    //Объявление аргументов из навигации
    private val args: GroupDetailsFragmentArgs by navArgs()

    //Объявление адаптера для пользователй
    private val userAdapter by lazy {
        UserAdapter() {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Инициализация ViewBinding и ViewModel
        viewModel = ViewModelProvider(this)[GroupListViewModel::class.java]
        _binding = FragmentGroupDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Метод для получения пользователей по их ID
    private suspend fun getUsersByIds(userIds: List<String>): MutableList<User> {
        //Получение из базы пользователей
        val db = Firebase.firestore
        val users = mutableListOf<User>()
        val collectionRef = db.collection("user")

        //Обработка полученных пользователей в цикле
        userIds.forEach { userId ->
            collectionRef.document(userId).get().await()?.toObject(User::class.java)?.let { user ->
                users.add(user)
            }
        }
        //Возвращение пользователей
        return users
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Подписка на изменение списка пользователей
        viewModel.userList.observe(viewLifecycleOwner) {
            userAdapter.setData(it)
            Log.d("develop", "obs")
        }
        // Инициализация Firebase Firestore
        val db = Firebase.firestore

        // Получение коллекции "group", документа с id=args.groupId и выполнение действия при успехе
        db.collection("group")
            .document(args.groupId)
            .get()
            .addOnSuccessListener { document ->
                // Проверка, что документ существует
                if (document != null) {
                    // Преобразование документа в объект класса Group
                    val group = document.toObject(Group::class.java)
                    // Получение списка участников группы
                    val members = group?.members ?: listOf()
                    // Заполнение полей на экране данными из полученного объекта group                    binding.etGroupName.setText(group?.name)
                    binding.etTask.setText(group?.task)

                    // Получение руководителя группы
                    CoroutineScope(Dispatchers.Main).launch {
                        // Обработка результата
                        val leaderUser = getUsersByIds(listOf(group?.leader) as List<String>)
                        val leader =
                            if (leaderUser.isNotEmpty()) "${leaderUser[0].surname} ${leaderUser[0].name}" else ""
                        binding.tvGroupLeader.text = "Руководитель группы: $leader"
                    }

                    //Получение работников
                    CoroutineScope(Dispatchers.Main).launch {
                        val users = getUsersByIds(members)
                        viewModel.setUsers(users)
                    }

                    //Установка данных в список
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

        //Сообщение о завершении работы
        binding.btnFinishWork.setOnClickListener {
            sendMessage(args.userName + Constants.FINISH_WORK_MESSAGE)
        }

        //Запрос звонка
        binding.btnRequestCall.setOnClickListener {
            sendMessage(args.userName + Constants.CALL_REQUEST)
        }

    }

    //Метод для отправки сообщения
    private fun sendMessage(message: String) {
        val db = Firebase.firestore

        val messageToServer = Message(
            id = UUID.randomUUID().toString(),
            sender = args.userId,
            data = message
        )
        db.collection("message").document().set(messageToServer)
            .addOnCompleteListener {
                Toast.makeText(requireContext(), "Отправлено", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Ошибка отправки", Toast.LENGTH_SHORT).show()
            }
    }
}