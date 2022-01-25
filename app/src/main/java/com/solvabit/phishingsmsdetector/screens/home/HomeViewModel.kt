package com.solvabit.phishingsmsdetector.screens.home

import android.content.Context
import android.database.Cursor
import android.provider.Telephony
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.solvabit.phishingsmsdetector.database.PhishingMessageDatabase
import com.solvabit.phishingsmsdetector.models.Message

class HomeViewModel(context: Context, private val cursor: Cursor): ViewModel() {

    private val _allMessages = MutableLiveData<List<Message>>()
    private val _msgList = MutableLiveData<List<Message>>()
    val msgList: LiveData<List<Message>>
        get() = _msgList

    val database = PhishingMessageDatabase.getDatabase(context)

    init {
        readSms()
    }

    // TODO: 25-01-2022 Function to get data from room 

    // TODO: 25-01-2022 Function to check if the id is in phishing 

    fun readSms() {

        val mutableMsgList = mutableListOf<Message>()
        while (cursor.moveToNext()) {
            val msg = Message().apply {
                _id = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms._ID))
                address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                person = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.PERSON))
                date = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
                date_sent = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE_SENT))
                read = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.READ))
                seen = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.SEEN))
                replyPathPresent = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.REPLY_PATH_PRESENT))
                subject = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.SUBJECT))
                body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                serviceCenter = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.SERVICE_CENTER))
                creator = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.CREATOR))
                type = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.TYPE))
            }
            mutableMsgList.add(msg)
        }
        _allMessages.value = mutableMsgList
        _msgList.value = mutableMsgList.distinctBy {
            it.address
        }
        Log.i(TAG, "readSms: ${_msgList.value.toString()}")
        cursor.close()
    }

    fun getList(address: String): Array<Message> {
        return _allMessages.value?.filter {
            it.address == address
        }?.toTypedArray() ?: arrayOf()
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}