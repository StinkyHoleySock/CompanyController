package com.example.companycontroller.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.companycontroller.data.model.Group
import com.example.companycontroller.databinding.ItemGroupBinding
/** Это адаптер для списка групп, который отображает название группы в элементе списка и обрабатывает нажатия на элементы списка.**/
class GroupAdapter(
    private val groupClickListener: (group: Group) -> Unit
) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    // список групп, которые будут отображаться
    private var groupList: MutableList<Group> = mutableListOf()

    // метод для обновления списка групп
    fun setData(data: List<Group>) {
        groupList.clear()
        groupList.addAll(data)
        notifyDataSetChanged()
    }

    // ViewHolder, отображающий элемент списка групп
    inner class GroupViewHolder(private val binding: ItemGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // метод для привязки данных группы к элементу списка
        fun bind(group: Group) {
            binding.tvGroupName.text = group.name

            binding.root.setOnClickListener {
                groupClickListener(group)
            }
        }
    }

    // создание ViewHolder для элемента списка групп
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(binding)
    }

    // привязка данных группы к ViewHolder
    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groupList[position]
        holder.bind(group)
    }

    // количество элементов в списке групп
    override fun getItemCount() = groupList.size
}