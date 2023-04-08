package com.example.companycontroller.shared.extensions

import android.content.Context
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

object ToastError {
    fun toast(context: Context, field: TextInputLayout) {
        Toast.makeText(
            context,
            "Поле '${field.hint.toString()}' не может быть пустым!",
            Toast.LENGTH_SHORT
        ).show()
    }
}