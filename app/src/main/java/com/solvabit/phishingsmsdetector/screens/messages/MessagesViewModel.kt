package com.solvabit.phishingsmsdetector.screens.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.solvabit.phishingsmsdetector.models.Message

class MessagesViewModel(val messages: List<Message>): ViewModel() {

    private val _allMessages = MutableLiveData<List<Message>>()
    val allMessages: LiveData<List<Message>>
        get() = _allMessages

    init {
        _allMessages.value = messages.reversed()
    }

}