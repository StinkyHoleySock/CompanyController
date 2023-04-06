package com.example.companycontroller.shared.extensions

import android.view.View

fun View.applyVisibility(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}