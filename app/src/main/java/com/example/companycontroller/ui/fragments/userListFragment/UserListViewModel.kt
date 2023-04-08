package com.example.companycontroller.ui.fragments.userListFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.companycontroller.data.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserListViewModel: ViewModel() {

    //LiveData для списка пользователй и индикатора загрузки
    private val _usersList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> get() = _usersList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val db = Firebase.firestore

    init {
        getUsers()
    }

    //Получение пользователей из firebase
    fun getUsers() {
        _isLoading.value = true
        db.collection("user")
            .get()
            .addOnSuccessListener { result ->
                val users = result.toObjects(User::class.java)

                _usersList.postValue(users)
                _isLoading.value = false
                Log.d("develop", "users: $users")
            }
            .addOnFailureListener { exception ->
                _isLoading.value = false
                Log.d("develop", "Error getting users", exception)
            }
    }

    //Поиск пользователей в списке
    fun filterUsersByName(query: String) {
        _isLoading.value = true
        db.collection("user")
            .whereEqualTo("name", query)
            .get()
            .addOnSuccessListener { result ->
                _isLoading.value = false
                val filteredUsers = result.toObjects(User::class.java)
                _usersList.postValue(filteredUsers)
            }
            .addOnFailureListener { exception ->
                _isLoading.value = false
                Log.d("develop", "Error filtering users by name", exception)
            }

//        db.collection("user")
//            .whereEqualTo("surname", query)
//            .get()
//            .addOnSuccessListener { result ->
//                _isLoading.value = false
//                val filteredUsers = result.toObjects(User::class.java)
//                _usersList.postValue(filteredUsers)
//            }
//            .addOnFailureListener { exception ->
//                _isLoading.value = false
//                Log.d("develop", "Error filtering users by name", exception)
//            }
    }

}