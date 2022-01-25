package com.solvabit.phishingsmsdetector.screens.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.solvabit.phishingsmsdetector.api.PhishingService
import com.solvabit.phishingsmsdetector.models.Message
import com.solvabit.phishingsmsdetector.models.Phishing
import com.solvabit.phishingsmsdetector.models.Phishing_Message
import retrofit2.Call
import retrofit2.Response

class MessageDetailsViewModel(val message: Message): ViewModel() {

    private val _hindiText = MutableLiveData<String>()
    val hindiText: LiveData<String>
        get() = _hindiText

    init {
        initializeTranslation(message.body)
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

    private fun initializeTranslation(messageText: String) {
        // Create an English-Hindi translator:
        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(FirebaseTranslateLanguage.EN)
            .setTargetLanguage(FirebaseTranslateLanguage.HI)
            .build()
        val englishHindiTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options)

        englishHindiTranslator.downloadModelIfNeeded()
            .addOnSuccessListener {
                englishHindiTranslator.translate(messageText)
                    .addOnSuccessListener { translatedText ->
                        _hindiText.value = translatedText
                    }
                    .addOnFailureListener { exception ->
                        _hindiText.value = exception.message
                    }
            }
            .addOnFailureListener {
                Log.i(TAG, "initializeTranslation: Error tranlating - ${it.message}")
            }
    }

    companion object {
        private const val TAG = "MessageDetailsViewModel"
    }
}