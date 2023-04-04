package com.example.companycontroller.ui.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.companycontroller.data.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserListViewModel: ViewModel() {

    private val _usersList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> get() = _usersList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val db = Firebase.firestore

    init {
        getUsers()
    }

    fun getUsers() {
        db.collection("user")
            .get()
            .addOnSuccessListener { result ->
                val users = result.toObjects(User::class.java)

                _usersList.postValue(users)
                Log.d("develop", "users: $users")
            }
            .addOnFailureListener { exception ->
                Log.d("develop", "Error getting users", exception)
            }
    }

}