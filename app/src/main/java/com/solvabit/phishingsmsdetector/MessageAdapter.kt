package com.solvabit.phishingsmsdetector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(val messageList: List<Message>): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_card,parent,false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.text.text = messageList[position].text
        holder.from.text = messageList[position].number
        holder.type.text = messageList[position].type
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val text  = itemView.findViewById<TextView>(R.id.text)
        val from  = itemView.findViewById<TextView>(R.id.from)
        val type  = itemView.findViewById<TextView>(R.id.type)
    }
}