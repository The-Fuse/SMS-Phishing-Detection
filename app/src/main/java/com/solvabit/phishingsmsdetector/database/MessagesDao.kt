package com.solvabit.phishingsmsdetector.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MessagesDao {

    @Insert
    suspend fun insertMessage(message: PhishedMessages)

    @Update
    suspend fun updateMessage(message: PhishedMessages)

    @Delete
    suspend fun deleteMessage(message: PhishedMessages)

    @Query("SELECT * from messages")
    fun getMessages(): LiveData<List<PhishedMessages>>
}