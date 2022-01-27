package com.solvabit.phishingsmsdetector.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface MessagesDao {

    @Insert
    suspend fun insertPhishedMessages(phishedMessages: PhishedMessages)

    @Update
    suspend fun updatePhishedMessages(phishedMessages: PhishedMessages)

    @Delete
    suspend fun deletePhishedMessages(phishedMessages: PhishedMessages)

    @Query("SELECT * from messages WHERE score < 50")
    fun getPhishedMessages(): LiveData<List<PhishedMessages>>

    @Query("SELECT * from messages WHERE _id == :key")
    suspend fun getMessageFromId(key: String): PhishedMessages?

    @Query("SELECT * from messages WHERE sender == :sender ORDER BY score DESC LIMIT 1")
    suspend fun getPhishedSender(sender: String): PhishedMessages?

    @Query("SELECT * from messages WHERE sender == :sender ORDER BY _id DESC")
    fun getAllPhishedMessagesOfSender(sender: String): LiveData<List<PhishedMessages>>

}