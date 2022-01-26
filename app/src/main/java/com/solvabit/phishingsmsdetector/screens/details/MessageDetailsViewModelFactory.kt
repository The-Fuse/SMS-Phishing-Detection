package com.solvabit.phishingsmsdetector.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.solvabit.phishingsmsdetector.database.PhishingMessageDatabase
import com.solvabit.phishingsmsdetector.models.Message
import java.lang.IllegalArgumentException

class MessageDetailsViewModelFactory(
    private val message: Message,
    private val database: PhishingMessageDatabase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MessageDetailsViewModel::class.java))
            return MessageDetailsViewModel(message, database) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}