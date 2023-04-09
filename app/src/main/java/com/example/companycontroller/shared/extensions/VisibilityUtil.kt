package com.example.companycontroller.shared.extensions

import android.view.View

//Функия-разширение для отображения элементов на экране
fun View.applyVisibility(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}