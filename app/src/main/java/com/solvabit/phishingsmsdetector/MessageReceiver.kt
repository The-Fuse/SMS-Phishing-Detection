package com.solvabit.phishingsmsdetector

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.solvabit.phishingsmsdetector.api.PhishingService
import com.solvabit.phishingsmsdetector.models.Phishing
import com.solvabit.phishingsmsdetector.models.Phishing_Message
import com.solvabit.phishingsmsdetector.utils.NotificationMessage
import com.solvabit.phishingsmsdetector.utils.sendNotification
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class MessageReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent!!.action) {
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                val notificationMessage = NotificationMessage().apply {
                    body = smsMessage.messageBody
                    sender = smsMessage.originatingAddress.toString()
                    _id = smsMessage.indexOnIcc
                }

                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager

                notificationManager.sendNotification(
                    notificationMessage,
                    context
                )

                val phishingMessage = Phishing_Message(notificationMessage.body)
                val phishingAPI = PhishingService.phishingAPInstance.checkPhishing(phishingMessage)
                phishingAPI.enqueue(object : retrofit2.Callback<Phishing> {
                    override fun onResponse(call: Call<Phishing>, response: Response<Phishing>) {
                        val result = response.body()
                        notificationMessage.apply {
                            this.result = result?.result ?: false
                            this.score = result?.score ?: 0
                        }
                        notificationManager.sendNotification(notificationMessage, context)
                    }

                    override fun onFailure(call: Call<Phishing>, t: Throwable) {
                        val error = t.message
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }

                })
            }
        }
    }

    fun sendNotification(context: Context?) {
        Toast.makeText(context, "API Hit", Toast.LENGTH_LONG).show()
    }

}