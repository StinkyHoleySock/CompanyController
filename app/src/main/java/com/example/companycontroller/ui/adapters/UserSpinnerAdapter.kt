package com.example.companycontroller.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.companycontroller.data.model.User

class UserSpinnerAdapter(private val context: Context, private val users: List<User>) : ArrayAdapter<User>(context, android.R.layout.simple_spinner_item, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
        val user = users[position]
        (view as TextView).text = "${user.surname} ${user.name}"
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        val user = users[position]
        (view as TextView).text = "${user.surname} ${user.name}"
        return view
    }
}