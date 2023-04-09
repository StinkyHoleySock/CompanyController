package com.example.companycontroller.ui.fragments.messages

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.companycontroller.data.model.Message
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MessageViewModel : ViewModel() {
    private val db = Firebase.firestore

    private val _messages = MutableLiveData<MutableList<Message>>()
    val messages: LiveData<MutableList<Message>> get() = _messages

    init {
        getMessages()
    }

    private fun getMessages() {
        db.collection("message")
            .get()
            .addOnSuccessListener { result ->
                val message = result.toObjects(Message::class.java)

                _messages.postValue(message)
                Log.d("develop", "mes: $message")
            }
    }
}