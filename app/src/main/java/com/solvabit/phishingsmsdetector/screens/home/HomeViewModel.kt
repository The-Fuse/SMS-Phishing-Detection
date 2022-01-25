package com.solvabit.phishingsmsdetector.screens.home

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.Telephony
import android.util.Log
import androidx.core.database.getLongOrNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solvabit.phishingsmsdetector.api.PhishingService
import com.solvabit.phishingsmsdetector.database.PhishedMessages
import com.solvabit.phishingsmsdetector.database.PhishingMessageDatabase
import com.solvabit.phishingsmsdetector.models.Message
import com.solvabit.phishingsmsdetector.models.Phishing
import com.solvabit.phishingsmsdetector.models.Phishing_Message
import retrofit2.Call
import retrofit2.Response

class HomeViewModel(context: Context, private val cursor: Cursor): ViewModel() {

    private val _allMessages = MutableLiveData<List<Message>>()
    private val _msgList = MutableLiveData<List<Message>>()
    val msgList: LiveData<List<Message>>
        get() = _msgList

    val database = PhishingMessageDatabase.getDatabase(context)

    init {
        readSms()
    }

    fun checkPhishing(message: Message) {

        val text = message.body.toString()
        val phishingMessage  = Phishing_Message(text)
        val phishingAPI = PhishingService.phishingAPInstance.checkPhishing(phishingMessage)
        phishingAPI.enqueue(object : retrofit2.Callback<Phishing> {
            override fun onResponse(call: Call<Phishing>, response: Response<Phishing>) {
                val reply = response.body()
                TODO("Not yet implemented")

//                val phishedMessage = PhishedMessages(message._id.toString(),reply.score,reply.result)
//
//                database.phishingMessagesDao().insertMessage(phishedMessage)

            }

            override fun onFailure(call: Call<Phishing>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun readSms()
    {

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