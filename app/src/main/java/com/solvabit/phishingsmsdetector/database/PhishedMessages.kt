package com.solvabit.phishingsmsdetector.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class PhishedMessages(
    @PrimaryKey
    val _id: String,
    val score: Int,
    val result: String,
    val sender: String
)
