package com.solvabit.phishingsmsdetector.screens.messages

import android.content.Context
import android.util.Log
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

private const val TAG = "MessagesViewModel"

class MessagesViewModel(val messages: List<Message>,  context: Context): ViewModel() {

    private val _allMessages = MutableLiveData<List<Message>>()
    val allMessages: LiveData<List<Message>>
        get() = _allMessages

    val database = PhishingMessageDatabase.getDatabase(context)

    private val _isPhished = MutableLiveData<Boolean>()
    val isPhished: LiveData<Boolean>
        get() = _isPhished

    private val _allPhishedMessageList = database.phishingMessagesDao().getAllPhishedMessagesOfSender(messages[0].address)
    val allPhishedMessagesList: LiveData<List<PhishedMessages>>
        get() = _allPhishedMessageList

    private val _mostPhished = MutableLiveData<Message>()
    val mostPhished : LiveData<Message>
        get() = _mostPhished

    init {
        viewModelScope.launch {
            val isPhishedMutable = getPhishedSender()
            Log.i(TAG, "$isPhishedMutable: ")
            if(isPhishedMutable == null) {
                _isPhished.value = false
            }
            else {
                if(isPhishedMutable.score < 50)
                    _isPhished.value = true
            }
        }
    }

    fun getPhishedList() {
        val msgList = _allPhishedMessageList.value
        msgList?.forEach { phishedMessage ->
            messages.find {
                it._id == phishedMessage._id.toInt()
            }?.creator = phishedMessage.score
        }
        _allMessages.value = messages.reversed()
    }

    suspend fun getPhishedSender(): PhishedMessages? {
        return withContext(Dispatchers.IO) {
            database.phishingMessagesDao().getPhishedSender(messages[0].address)
        }
    }



}