package com.example.companycontroller.ui.fragments.groupListFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.companycontroller.data.model.Group
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GroupListViewModel: ViewModel() {

    //LiveData для списка пользователй и индикатора загрузки
    private val _groupsList = MutableLiveData<List<Group>>()
    val groupList: LiveData<List<Group>> get() = _groupsList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val db = Firebase.firestore

    init {
        getGroups()
    }

    //Получение пользователей из firebase
    fun getGroups() {
        _isLoading.value = true
        db.collection("group")
            .get()
            .addOnSuccessListener { result ->
                val groups = result.toObjects(Group::class.java)

                _groupsList.postValue(groups)
                _isLoading.value = false
                Log.d("develop", "groups: $groups")
            }
            .addOnFailureListener { exception ->
                _isLoading.value = false
                Log.d("develop", "Error getting groups", exception)
            }
    }

    //Поиск пользователей в списке
    fun filterGroupsByName(query: String) {
        _isLoading.value = true
        db.collection("group")
            .whereEqualTo("name", query)
            .get()
            .addOnSuccessListener { result ->
                _isLoading.value = false
                val filteredGroups = result.toObjects(Group::class.java)
                _groupsList.postValue(filteredGroups)
            }
            .addOnFailureListener { exception ->
                _isLoading.value = false
                Log.d("develop", "Error filtering groups by name", exception)
            }

//        db.collection("group")
//            .whereEqualTo("surname", query)
//            .get()
//            .addOnSuccessListener { result ->
//                _isLoading.value = false
//                val filteredGroups = result.toObjects(Group::class.java)
//                _groupsList.postValue(filteredGroups)
//            }
//            .addOnFailureListener { exception ->
//                _isLoading.value = false
//                Log.d("develop", "Error filtering groups by name", exception)
//            }
    }

}