package com.solvabit.phishingsmsdetector.screens.messages

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import com.solvabit.phishingsmsdetector.models.Message

class MessagesViewModelFactory(private val messages: List<Message>, private val requireContext: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MessagesViewModel::class.java))
            return MessagesViewModel(messages, requireContext) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}