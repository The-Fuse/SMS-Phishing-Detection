package com.solvabit.phishingsmsdetector.screens.messages

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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

    private val _mostPhished = MutableLiveData<PhishedMessages>()
    val mostPhished : LiveData<PhishedMessages>
        get() = _mostPhished

    private val _mostPhishedMessage = MutableLiveData<Message>()
    val mostPhishedMessage : LiveData<Message>
        get() = _mostPhishedMessage

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

    @RequiresApi(Build.VERSION_CODES.N)
    fun getPhishedList() {
        val msgList = _allPhishedMessageList.value
        msgList?.forEach { phishedMessage ->
            messages.find {
                it._id == phishedMessage._id.toInt()
            }?.creator = phishedMessage.score
        }
        _allMessages.value = messages.reversed()
        _mostPhished.value = _allPhishedMessageList.value?.minWithOrNull(Comparator.comparingInt { it.score })
        getMostPhishedMessage()
    }

    private fun getMostPhishedMessage() {
         val mostPhishedList = messages.filter { it._id == _mostPhished.value?._id?.toInt() }
        _mostPhishedMessage.value = when(mostPhishedList.size) {
            0 -> return
            else -> mostPhishedList[0]
        }
        Log.i(TAG, "getMostPhishedMessage: ${_mostPhishedMessage.value}")
    }

    suspend fun getPhishedSender(): PhishedMessages? {
        return withContext(Dispatchers.IO) {
            database.phishingMessagesDao().getPhishedSender(messages[0].address)
        }
    }



}