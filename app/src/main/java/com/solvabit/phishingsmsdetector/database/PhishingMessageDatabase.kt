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
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PhishingMessageDatabase::class.java,
                        "messagesDB"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}