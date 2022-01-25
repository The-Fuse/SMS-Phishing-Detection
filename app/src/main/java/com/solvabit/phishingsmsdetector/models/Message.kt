package com.solvabit.phishingsmsdetector.models

data class Message(
    val _id: Int,
    val address: Long,
    val person: String?,    // ID of the sender of the conversation, if present
    val date: Long,         // date the message was received
    val date_sent: Long,    // date the message was sent
    val read: Int,          // Has the message been read?
    val seen: Int,          // Has the message been seen by the user? The "seen" flag determines whether we need to show a notification
    val reply_path_present: Int,
    val subject: String?,
    val body: String,
    val service_center: String?,
    val creator: String?
)
