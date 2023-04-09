package com.example.companycontroller.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.companycontroller.data.model.User
import com.example.companycontroller.databinding.ItemUserBinding

/** Это адаптер для списка пользователей, который отображает данные в элементе списка и обрабатывает нажатия на элементы списка.**/

class UserAdapter(
    private val userClickListener: (user: User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    private var userList: MutableList<User> = mutableListOf()

    fun setData(data: List<User>) {
        userList.clear()
        userList.addAll(data)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvUserName.text = "${user.surname} ${user.name} ${user.patronymic}"

            binding.root.setOnClickListener {
                userClickListener(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount() = userList.size
}