package com.solvabit.phishingsmsdetector.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PhishedMessages::class], version = 1)
abstract class PhishingMessageDatabase : RoomDatabase() {
    abstract fun phishingMessagesDao(): MessagesDao

    companion object {
        @Volatile
        private var INSTANCE: PhishingMessageDatabase? = null

        fun getDatabase(context: Context): PhishingMessageDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        PhishingMessageDatabase::class.java,
                        "messagesDB"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}