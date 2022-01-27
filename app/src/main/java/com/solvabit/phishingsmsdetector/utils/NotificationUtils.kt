package com.solvabit.phishingsmsdetector.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.solvabit.phishingsmsdetector.MainActivity
import com.solvabit.phishingsmsdetector.R
import com.solvabit.phishingsmsdetector.models.Message

// Notification ID.
private const val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

private const val TAG = "NotificationUtils"


fun NotificationManager.sendNotification(message: NotificationMessage, applicationContext: Context) {

    val messageObj = Message()
    messageObj.body = message.body
    messageObj._id = message._id
    messageObj.address = message.sender
    Log.i(TAG, "sendNotification: ${message.sender}")

    val bundle = Bundle()
    bundle.putParcelable("message", messageObj)
    val contentIntent = NavDeepLinkBuilder(applicationContext)
        .setComponentName(MainActivity::class.java)
        .setGraph(R.navigation.main_nav_graph)
        .setDestination(R.id.messageDetailsFragment)
        .setArguments(bundle)
        .createPendingIntent()

    // Get the layouts to use in the custom notification

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.phishing_notification_channel_id)
    )

    val notificationLayout: RemoteViews = when(message.score) {
        -1 -> {
            val tempLayout = RemoteViews("com.solvabit.phishingsmsdetector", R.layout.notification_small_processing)
            builder.priority = NotificationCompat.PRIORITY_HIGH
            tempLayout
        }
        else -> {
            val tempLayout = RemoteViews("com.solvabit.phishingsmsdetector", R.layout.notification_small)
            tempLayout.setImageViewResource(R.id.small_notification_imageView, R.drawable.warning_icon)
            tempLayout.setTextViewText(R.id.small_notification_score_text, "Unsafe Percentage: " + message.score + "%")
            builder.priority = NotificationCompat.PRIORITY_HIGH
            tempLayout
        }

    }

//    val notificationLayoutExpanded =
//        RemoteViews("com.solvabit.phishingsmsdetector", R.layout.notification_large)

    // Build the notification
    builder
        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
        .setCustomContentView(notificationLayout)
//        .setCustomBigContentView(notificationLayoutExpanded)

        .setContentIntent(contentIntent)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}


fun NotificationManager.cancelNotifications() {
    cancelAll()
}

data class NotificationMessage(
    var body: String = "",
    var sender: String = "",
    var result: Boolean = false,
    var score: Int = -1,
    var _id: Int = -1
)
