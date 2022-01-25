package com.solvabit.phishingsmsdetector.screens.details

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solvabit.phishingsmsdetector.api.PhishingService
import com.solvabit.phishingsmsdetector.models.Message
import com.solvabit.phishingsmsdetector.models.Phishing
import com.solvabit.phishingsmsdetector.models.Phishing_Message
import retrofit2.Call
import retrofit2.Response

class MessageDetailsViewModel(val message: Message): ViewModel() {

    init {
        //initializeTranslation()
        checkPhishing(message)
    }

    private fun checkPhishing(message: Message) {

        val text = message.body.toString()
        val phishingMessage  = Phishing_Message(text)
        val phishingAPI = PhishingService.phishingAPInstance.checkPhishing(phishingMessage)
        phishingAPI.enqueue(object : retrofit2.Callback<Phishing> {
            override fun onResponse(call: Call<Phishing>, response: Response<Phishing>) {
                val reply = response.body()
                Log.d("details",reply.toString())

//                val phishedMessage = PhishedMessages(message._id.toString(),reply.score,reply.result)
//
//                database.phishingMessagesDao().insertMessage(phishedMessage)

            }

            override fun onFailure(call: Call<Phishing>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun initializeTranslation() {
        TODO("Not yet implemented")
    }
}