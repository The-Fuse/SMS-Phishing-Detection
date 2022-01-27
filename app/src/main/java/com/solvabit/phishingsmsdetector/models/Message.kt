package com.solvabit.phishingsmsdetector.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    var _id: Int = -1,
    var address: String = "",
    var person: String? = "",    // ID of the sender of the conversation, if present
    var date: Long = -1,         // date the message was received
    var date_sent: Long = -1,    // date the message was sent
    var read: Int = -1,          // Has the message been read?
    var seen: Int = -1,          // Has the message been seen by the user? The "seen" flag determines whether we need to show a notification
    var replyPathPresent: Int = -1,
    var subject: String? = "",
    var body: String = "",
    var serviceCenter: String? = "",
    var creator: Int = -1,
    var type: Int = 0
): Parcelable

