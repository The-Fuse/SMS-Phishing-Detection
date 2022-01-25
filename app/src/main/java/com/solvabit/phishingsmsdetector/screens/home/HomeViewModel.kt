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


        val idCol = Telephony.TextBasedSmsColumns.THREAD_ID
        val addressCol = Telephony.TextBasedSmsColumns.ADDRESS
        val personCol = Telephony.TextBasedSmsColumns.PERSON
        val dateCol = Telephony.TextBasedSmsColumns.DATE
        val dateSentCol = Telephony.TextBasedSmsColumns.DATE_SENT
        val readCol = Telephony.TextBasedSmsColumns.READ
        val seenCol = Telephony.TextBasedSmsColumns.SEEN
        val replyPathCol = Telephony.TextBasedSmsColumns.REPLY_PATH_PRESENT
        val subjectCol = Telephony.TextBasedSmsColumns.SUBJECT
        val bodyCol = Telephony.TextBasedSmsColumns.BODY
        val serviceCol = Telephony.TextBasedSmsColumns.SERVICE_CENTER
        val creatorCol = Telephony.TextBasedSmsColumns.CREATOR
        val typeCol = Telephony.TextBasedSmsColumns.TYPE // 1 - Inbox, 2 - Sent
        val projection = arrayOf(idCol,addressCol,personCol,dateCol,dateSentCol,readCol,seenCol,replyPathCol,subjectCol,bodyCol,serviceCol,creatorCol,typeCol)

        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            projection, null, null, null
        )

        val idColIdx = cursor!!.getColumnIndex(idCol)
        val addressColIdx = cursor.getColumnIndex(addressCol)
        val personColIdx = cursor.getColumnIndex(personCol)
        val dateColIdx = cursor.getColumnIndex(dateCol)
        val dateSentColIdx = cursor.getColumnIndex(dateSentCol)
        val readColIdx = cursor.getColumnIndex(readCol)
        val seenColIdx = cursor.getColumnIndex(seenCol)
        val replyPathColIdx = cursor.getColumnIndex(replyPathCol)
        val subjectColIdx = cursor.getColumnIndex(subjectCol)
        val bodyColIdx = cursor.getColumnIndex(bodyCol)
        val serviceColIdx = cursor.getColumnIndex(serviceCol)
        val creatorColIdx = cursor.getColumnIndex(creatorCol)
        val typeColIdx = cursor.getColumnIndex(typeCol)

        val mutableMsgList = mutableListOf<Message>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(idColIdx)
            val address = cursor.getLong(addressColIdx)
            val person = cursor.getString(personColIdx)
            val date = cursor.getLong(dateColIdx)
            val dateSent = cursor.getLong(dateSentColIdx)
            val read = cursor.getInt(readColIdx)
            val seen = cursor.getInt(seenColIdx)
            val reply_path_present = cursor.getInt(replyPathColIdx)
            val subject = cursor.getString(subjectColIdx)
            val body = cursor.getString(bodyColIdx)
            val service_center = cursor.getString(serviceColIdx)
            val creator = cursor.getString(creatorColIdx)
            val type = cursor.getInt(typeColIdx)

            val msg = Message(id,address,person,date,dateSent,read,seen,reply_path_present,subject,body,service_center,creator,type)
            mutableMsgList.add(msg)
        }
        _msgList.value = mutableMsgList

        Log.i("MainActivity",mutableMsgList.toString())
        cursor.close()
    }
}