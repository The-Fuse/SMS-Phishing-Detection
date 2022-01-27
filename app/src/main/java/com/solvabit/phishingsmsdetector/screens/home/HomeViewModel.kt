package com.solvabit.phishingsmsdetector.screens.home

import android.content.Context
import android.database.Cursor
import android.provider.Telephony
import android.util.Log
import androidx.lifecycle.*
import com.solvabit.phishingsmsdetector.database.PhishedMessages
import com.solvabit.phishingsmsdetector.database.PhishingMessageDatabase
import com.solvabit.phishingsmsdetector.models.Message
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HomeViewModel(context: Context, private val cursor: Cursor) : ViewModel() {

    private val mutableMsgList = mutableListOf<Message>()

    private val _allMessages = MutableLiveData<List<Message>>()
    private val _msgList = MutableLiveData<List<Message>>()
    val msgList: LiveData<List<Message>>
        get() = _msgList


    val database = PhishingMessageDatabase.getDatabase(context)

    private var _phishedList = database.phishingMessagesDao().getPhishedMessages()
    val phishedList: LiveData<List<PhishedMessages>>
        get() = _phishedList

    val lifeDataMerger = MediatorLiveData<List<PhishedMessages>>()

    init {
        readSms()
    }

    fun readSms() {
        while (cursor.moveToNext()) {
            val msg = Message().apply {
                _id = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms._ID))
                address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                person = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.PERSON))
                date = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
                date_sent = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE_SENT))
                read = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.READ))
                seen = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.SEEN))
                replyPathPresent =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.REPLY_PATH_PRESENT))
                subject = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.SUBJECT))
                body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                serviceCenter =
                    cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.SERVICE_CENTER))
                creator = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.CREATOR))
                type = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.TYPE))
            }
            mutableMsgList.add(msg)
        }
        cursor.close()
    }

    fun refreshListPhishedData() {
        val msgListMutable = mutableMsgList.distinctBy {
            it.address
        }
        val phishedDataMutable = _phishedList.value
        Log.i(TAG, "getPhishingData: $phishedDataMutable")
        phishedDataMutable?.forEach { phishedMessage ->
            msgListMutable.find {
                Log.i(TAG, "getPhishingData: ${phishedMessage.sender} == ${it.address}")
                phishedMessage.sender == it.address
            }?.type = -1
        }
        _allMessages.value = msgListMutable
        _msgList.value = msgListMutable
    }

    fun getList(address: String): Array<Message> {
        return _allMessages.value?.filter {
            it.address == address
        }?.toTypedArray() ?: arrayOf()
    }

    fun selectCompany(query: String) {
        val copyList = _allMessages.value
        _msgList.value = copyList?.filter {
            it.body.contains(query, true) || it.address.contains(query, true)
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}