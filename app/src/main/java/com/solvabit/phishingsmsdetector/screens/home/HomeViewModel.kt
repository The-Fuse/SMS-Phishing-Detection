package com.solvabit.phishingsmsdetector.screens.home

import android.content.ContentResolver
import android.database.Cursor
import android.provider.Telephony
import android.util.Log
import androidx.core.database.getLongOrNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.solvabit.phishingsmsdetector.models.Message

class HomeViewModel(private val contentResolver: ContentResolver): ViewModel() {

    private val _msgList = MutableLiveData<List<Message>>()
    val msgList: LiveData<List<Message>>
        get() = _msgList


    init {
        readSms()
    }

    fun readSms()
    {
        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null, null, null, null
        ) ?: return

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
        _msgList.value = mutableMsgList.distinctBy {
            it.address
        }
        Log.i(TAG, "readSms: ${_msgList.value.toString()}")
        cursor.close()
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}