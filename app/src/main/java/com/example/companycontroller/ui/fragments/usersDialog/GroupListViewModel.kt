package com.example.companycontroller.ui.fragments.usersDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companycontroller.data.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GroupListViewModel: ViewModel() {

    //LiveData для подписки на изменение данных
    private val _usersList = MutableLiveData<MutableList<User>>()
    val userList: LiveData<MutableList<User>> get() = _usersList


    fun setUsers(users: MutableList<User>) {
        _usersList.value = users
    }

    fun deleteUser(user: User) {
        _usersList.value?.remove(user)
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

    fun getUsersList(userIds: List<String>) {
        viewModelScope.launch {
            _usersList.value = getUsersByIds(userIds)
        }
    }
}