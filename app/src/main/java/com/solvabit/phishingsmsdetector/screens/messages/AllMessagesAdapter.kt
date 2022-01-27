package com.solvabit.phishingsmsdetector.screens.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.solvabit.phishingsmsdetector.databinding.MessageCardBinding
import com.solvabit.phishingsmsdetector.models.Message
import com.solvabit.phishingsmsdetector.screens.home.HomeDiffCallback

class AllMessagesAdapter(private val messagesListener: MessagesListener):
    ListAdapter<Message, AllMessagesAdapter.ViewHolder>(HomeDiffCallback()){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllMessagesAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AllMessagesAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position), messagesListener)
    }

    class ViewHolder private constructor(private val binding: MessageCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message, messagesListener: MessagesListener) {
            binding.message = item
            binding.clickListener = messagesListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MessageCardBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

}

class MessagesListener(val clickListener: (message: Message) -> Unit) {
    fun onClick(message: Message) = clickListener(message)
}