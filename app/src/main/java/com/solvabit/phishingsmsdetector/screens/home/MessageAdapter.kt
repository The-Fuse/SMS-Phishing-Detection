package com.solvabit.phishingsmsdetector.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.solvabit.phishingsmsdetector.databinding.SenderCardBinding
import com.solvabit.phishingsmsdetector.models.Message

class HomeAdapter(private val homeAdapterListener: HomeAdapterListener) :
    ListAdapter<Message, HomeAdapter.ViewHolder>(HomeDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, homeAdapterListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(private val binding: SenderCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message, homeAdapterListener: HomeAdapterListener) {
            binding.message = item
            binding.clickListener = homeAdapterListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SenderCardBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class HomeDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }

}

class HomeAdapterListener(val clickListener: (message: Message) -> Unit) {
    fun onClick(message: Message) = clickListener(message)
}