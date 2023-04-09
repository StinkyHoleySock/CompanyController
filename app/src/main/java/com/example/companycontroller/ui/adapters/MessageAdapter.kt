package com.example.companycontroller.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.companycontroller.data.model.Message
import com.example.companycontroller.databinding.ItemMessageBinding

class MessageAdapter() : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messages: MutableList<Message> = mutableListOf()

    fun setData(data: List<Message>) {
        messages.clear()
        messages.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount() = messages.size

    inner class MessageViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.tvMessage.text = message.data
        }
    }
}