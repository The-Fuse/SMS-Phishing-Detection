package com.solvabit.phishingsmsdetector.screens.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.solvabit.phishingsmsdetector.api.PhishingService
import com.solvabit.phishingsmsdetector.database.PhishedMessages
import com.solvabit.phishingsmsdetector.database.PhishingMessageDatabase
import com.solvabit.phishingsmsdetector.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import kotlin.properties.Delegates

enum class STATUS{Loading, Success, Failed}

class MessageDetailsViewModel(val message: Message, val database: PhishingMessageDatabase) :
    ViewModel() {

    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score
    private val _report = MutableLiveData<Boolean>()
    val report : LiveData<Boolean>
        get() = _report
    private val _hindiText = MutableLiveData<String>()
    val hindiText: LiveData<String>
        get() = _hindiText

    private val _youtubeList = MutableLiveData<List<Items>>()
    val youtubeList: LiveData<List<Items>>
        get() = _youtubeList

    private val _youtubeStatus = MutableLiveData<STATUS>()
    val youtubeStatus: LiveData<STATUS>
        get() = _youtubeStatus

    private val _hindiStatus = MutableLiveData<STATUS>()
    val hindiStatus: LiveData<STATUS>
        get() = _hindiStatus

    private val _apiStatus = MutableLiveData<STATUS>()
    val apiStatus: LiveData<STATUS>
        get() = _apiStatus

    init {
        initializeTranslation(message.body)
        initializeTwitterData()
        initializeYoutubeData()
        initializeCheckDataExists()
    }

    private fun initializeTwitterData() {
        Log.i(TAG, "initializeYoutubeData: ${message.body.take(100)}")
        val twitterAPI = PhishingService.twitterAPInstance.getTweets("narendra modi")
        twitterAPI.enqueue(object : retrofit2.Callback<Tweets> {
            override fun onResponse(call: Call<Tweets>, response: Response<Tweets>) {
                val result = response.body()
                Log.i(TAG, "onResponse Twitter: $result")
            }

            override fun onFailure(call: Call<Tweets>, t: Throwable) {
                Log.i(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun initializeYoutubeData() {
        Log.i(TAG, "initializeYoutubeData: ${message.body.take(100)}")
        _youtubeStatus.value = STATUS.Loading
        val ytAPI = PhishingService.youtubeAPInstance.getYoutubeVideos(message.body.take(100))
        ytAPI.enqueue(object : retrofit2.Callback<YoutubeData> {
            override fun onResponse(call: Call<YoutubeData>, response: Response<YoutubeData>) {
                _youtubeList.value = response.body()?.items
                _youtubeStatus.value = STATUS.Success
                Log.i(TAG, "onResponse: ${_youtubeList.value}")
            }

            override fun onFailure(call: Call<YoutubeData>, t: Throwable) {
                _youtubeStatus.value = STATUS.Failed
                Log.i(TAG, "onFailure: ${t.message}")
            }
        })

    }

    private fun initializeCheckDataExists() {
        _apiStatus.value = STATUS.Loading
        when (message._id) {
            -1 -> {
                checkPhishing(message)
                Log.i(TAG, "when : ${message._id} ")
            }
            else -> viewModelScope.launch {
                Log.i(TAG, "when : ${message._id} ")
                getPhishingData(message)
            }
        }
    }

    private suspend fun getPhishingData(message: Message) {

        val msgData = withContext(Dispatchers.IO) {
            database.phishingMessagesDao().getMessageFromId(message._id.toString())
        }

        Log.i(TAG, "outside withContext : ${msgData} ")
        if (msgData == null) {
            checkPhishing(message)
        }else{
            _score.value = msgData.score
            _report.value = msgData.result
            _apiStatus.value = STATUS.Success
        }
    }

    private fun checkPhishing(message: Message) {
        Log.i(TAG, "inside checkPhishing : ${message._id} ")
        val text = message.body
        val phishingMessage = Phishing_Message(text)
        val phishingAPI = PhishingService.phishingAPInstance.checkPhishing(phishingMessage)
        phishingAPI.enqueue(object : retrofit2.Callback<Phishing> {
            override fun onResponse(call: Call<Phishing>, response: Response<Phishing>) {
                val reply = response.body()
                viewModelScope.launch {

                    val point = 100- (reply?.score!!) ?: 0
                    val phishedMessage =
                        PhishedMessages(
                            message._id.toString(),
                            point,
                            reply?.result ?: false,
                            message.address
                        )
                    _score.value = point
                    _report.value = reply.result
                    _apiStatus.value = STATUS.Success
                    if(message._id != -1)
                        addPhishedDataToRoom(phishedMessage)
                }
            }

            override fun onFailure(call: Call<Phishing>, t: Throwable) {
                _apiStatus.value = STATUS.Failed
                Log.i(TAG, "onFailure this: ${t.message}")
            }
        })

    }

    private suspend fun addPhishedDataToRoom(phishedMessage: PhishedMessages) {
        database.phishingMessagesDao().insertPhishedMessages(phishedMessage)
    }

    private fun initializeTranslation(messageText: String) {

        _hindiStatus.value = STATUS.Loading
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
                        _hindiStatus.value = STATUS.Success
                    }
                    .addOnFailureListener { exception ->
                        _hindiText.value = exception.message
                    }
            }
            .addOnFailureListener {
                _hindiStatus.value = STATUS.Failed
            }
    }

    companion object {
        private const val TAG = "MessageDetailsViewModel"
    }
}