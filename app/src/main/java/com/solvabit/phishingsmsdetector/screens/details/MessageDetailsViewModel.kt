package com.solvabit.phishingsmsdetector.screens.details

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.solvabit.phishingsmsdetector.models.Message
import kotlinx.coroutines.launch

class MessageDetailsViewModel(val message: Message): ViewModel() {

    private val _hindiText = MutableLiveData<String>()
    val hindiText: LiveData<String>
        get() = _hindiText

    init {
        initializeTranslation(message.body)
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