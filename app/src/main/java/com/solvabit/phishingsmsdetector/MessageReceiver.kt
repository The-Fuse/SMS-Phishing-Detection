package com.solvabit.phishingsmsdetector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import java.lang.Exception


class MessageReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent!!.action) {
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                val message = smsMessage.messageBody
                val from = smsMessage.originatingAddress
                Log.i("Message",message+from)
                Toast.makeText(context,message+from,Toast.LENGTH_LONG).show()
            }
        }
    }

}