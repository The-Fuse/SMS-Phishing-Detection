package com.solvabit.phishingsmsdetector.database

import androidx.room.Entity

@Entity(tableName = "messages")
data class PhishedMessages(
    val _id: String,
    val score: Int,
    val result: String
)
