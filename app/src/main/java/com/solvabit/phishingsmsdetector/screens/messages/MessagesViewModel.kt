package com.solvabit.phishingsmsdetector.screens.messages

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solvabit.phishingsmsdetector.database.PhishedMessages
import com.solvabit.phishingsmsdetector.database.PhishingMessageDatabase
import com.solvabit.phishingsmsdetector.models.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessagesViewModel(val messages: List<Message>,  context: Context): ViewModel() {

    private val _allMessages = MutableLiveData<List<Message>>()
    val allMessages: LiveData<List<Message>>
        get() = _allMessages

    val database = PhishingMessageDatabase.getDatabase(context)

    private val _isPhished = MutableLiveData<Boolean>()
    val isPhished: LiveData<Boolean>
        get() = _isPhished


    init {
        _allMessages.value = messages.reversed()
        viewModelScope.launch {
            val isPhishedMutable = getPhishedSender()
            _isPhished.value = isPhishedMutable==null || isPhishedMutable.score > 50
        }
    }

    suspend fun getPhishedSender(): PhishedMessages? {
        return withContext(Dispatchers.IO) {
            database.phishingMessagesDao().getPhishedSender(messages[0].address)
        }
    }



}