package com.solvabit.phishingsmsdetector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.solvabit.phishingsmsdetector.api.PhishingService
import com.solvabit.phishingsmsdetector.models.Phishing
import com.solvabit.phishingsmsdetector.models.Phishing_Message
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class MessageReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent!!.action) {
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                val message = smsMessage.messageBody
                val from = smsMessage.originatingAddress
                Log.i("Message", message + from)
                // Toast.makeText(context, message + from, Toast.LENGTH_LONG).show()
                val phishingMessage  = Phishing_Message(message)
                val phishingAPI = PhishingService.phishingAPInstance.checkPhishing(phishingMessage)
                phishingAPI.enqueue(object : retrofit2.Callback<Phishing> {
                    override fun onResponse(call: Call<Phishing>, response: Response<Phishing>) {
                        val result = response.body()
                        Toast.makeText(context,result.toString(),Toast.LENGTH_LONG).show()
                    }

                    override fun onFailure(call: Call<Phishing>, t: Throwable) {
                        val error = t.message
                        Toast.makeText(context,error,Toast.LENGTH_LONG).show()
                    }

                })
            }
        }
    }
    fun sendNotification(context: Context?){
        Toast.makeText(context,  "API Hit", Toast.LENGTH_LONG).show()
    }



}