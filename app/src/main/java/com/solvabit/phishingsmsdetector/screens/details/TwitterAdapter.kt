package com.solvabit.phishingsmsdetector.screens.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.solvabit.phishingsmsdetector.databinding.TweetsCardBinding
import com.solvabit.phishingsmsdetector.models.Tweet

class TwitterAdapter(private val twitterListener: TwitterListener):
    ListAdapter<Tweet, TwitterAdapter.ViewHolder>(TwitterDiffCallback())
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TwitterAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TwitterAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position), twitterListener)
    }

    class ViewHolder private constructor(private val binding: TweetsCardBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(item: Tweet, clickListener: TwitterListener) {
                    binding.tweet = item
                    binding.executePendingBindings()
                }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TweetsCardBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
            }
}

class TwitterDiffCallback: DiffUtil.ItemCallback<Tweet>() {
    override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet): Boolean {
        return oldItem == newItem
    }

}

class TwitterListener(val clickListener: (item: Tweet) -> Unit) {
    fun onClick(item: Tweet) = clickListener(item)
}