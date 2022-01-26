package com.solvabit.phishingsmsdetector.screens.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.solvabit.phishingsmsdetector.databinding.YoutubeVideoBinding
import com.solvabit.phishingsmsdetector.models.Items


class YoutubeAdapter(private val youtubeListener: YoutubeListener):
ListAdapter<Items, YoutubeAdapter.ViewHolder>(YoutubeDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), youtubeListener)
    }

    class ViewHolder private constructor(private val binding: YoutubeVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Items, clickListener: YoutubeListener) {
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = YoutubeVideoBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }
}

class YoutubeDiffCallback: DiffUtil.ItemCallback<Items>() {
    override fun areItemsTheSame(oldItem: Items, newItem: Items): Boolean {
        return oldItem.id.videoId == newItem.id.videoId
    }

    override fun areContentsTheSame(oldItem: Items, newItem: Items): Boolean {
        return oldItem == newItem
    }
}

class YoutubeListener(val clickListener: (item: Items) -> Unit) {
    fun onClick(item: Items) = clickListener(item)
}