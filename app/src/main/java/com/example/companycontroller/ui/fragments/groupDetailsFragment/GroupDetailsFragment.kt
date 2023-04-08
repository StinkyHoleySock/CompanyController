package com.example.companycontroller.ui.fragments.groupDetailsFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companycontroller.R
import com.example.companycontroller.data.model.Group
import com.example.companycontroller.data.model.User
import com.example.companycontroller.databinding.FragmentGroupDetailsBinding
import com.example.companycontroller.ui.adapters.UserAdapter
import com.example.companycontroller.ui.fragments.usersDialog.GroupListViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GroupDetailsFragment : Fragment(R.layout.fragment_group_details) {

    private var _binding: FragmentGroupDetailsBinding? = null
    private val binding: FragmentGroupDetailsBinding get() = _binding!!
    private lateinit var viewModel: GroupListViewModel

    private val args: GroupDetailsFragmentArgs by navArgs()

    private val userAdapter by lazy {
        UserAdapter() {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[GroupListViewModel::class.java]
        _binding = FragmentGroupDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

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

        viewModel.userList.observe(viewLifecycleOwner) {
            userAdapter.setData(it)
            Log.d("develop", "obs")
        }

        val db = Firebase.firestore
        db.collection("group")
            .document(args.groupId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val group = document.toObject(Group::class.java)
                    val members = group?.members ?: listOf()

                    binding.etGroupName.setText(group?.name)
                    binding.etTask.setText(group?.task)

                    CoroutineScope(Dispatchers.Main).launch {
                        // Обработка результата
                        val leaderUser = getUsersByIds(listOf(group?.leader) as List<String>)
                        val leader =
                            if (leaderUser.isNotEmpty()) "${leaderUser[0].surname} ${leaderUser[0].name}" else ""
                        binding.tvGroupLeader.text = "Руководитель группы: $leader"
                    }

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

    }
}