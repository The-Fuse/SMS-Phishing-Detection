package com.solvabit.phishingsmsdetector.screens.home

import android.content.ContentResolver
import android.database.Cursor
import android.provider.Telephony
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
        val numberCol = Telephony.TextBasedSmsColumns.ADDRESS
        val textCol = Telephony.TextBasedSmsColumns.BODY
        val typeCol = Telephony.TextBasedSmsColumns.TYPE // 1 - Inbox, 2 - Sent

        val projection = arrayOf(numberCol, textCol, typeCol)

        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            projection, null, null, null
        )

        val numberColIdx = cursor!!.getColumnIndex(numberCol)
        val textColIdx = cursor.getColumnIndex(textCol)
        val typeColIdx = cursor.getColumnIndex(typeCol)

        val mutableMsgList = mutableListOf<Message>()
        while (cursor.moveToNext()) {
            val number = cursor.getString(numberColIdx)
            val text = cursor.getString(textColIdx)
            val type = cursor.getString(typeColIdx)

            val msg = Message(text,number,type)
            mutableMsgList.add(msg)
        }
        _msgList.value = mutableMsgList

        cursor.close()
    }
}