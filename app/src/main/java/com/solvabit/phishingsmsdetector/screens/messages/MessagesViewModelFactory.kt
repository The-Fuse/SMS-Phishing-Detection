package com.solvabit.phishingsmsdetector.screens.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import com.solvabit.phishingsmsdetector.models.Message

class MessagesViewModelFactory(private val messages: List<Message>): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MessagesViewModel::class.java))
            return MessagesViewModel(messages) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}