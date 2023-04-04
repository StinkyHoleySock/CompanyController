package com.example.companycontroller.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.companycontroller.data.model.Group
import com.example.companycontroller.databinding.ItemGroupBinding

class GroupAdapter(
    private val groupClickListener: (group: Group) -> Unit
) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    private var groupList: MutableList<Group> = mutableListOf()

    fun setData(data: List<Group>) {
        groupList.clear()
        groupList.addAll(data)
        notifyDataSetChanged()
    }

    inner class GroupViewHolder(private val binding: ItemGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(group: Group) {
            binding.tvGroupName.text = group.name

            binding.root.setOnClickListener {
                groupClickListener(group)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groupList[position]
        holder.bind(group)
    }

    override fun getItemCount() = groupList.size
}