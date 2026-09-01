package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        PostEntity::class,
        CommentEntity::class,
        DraftEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BalooraDatabase : RoomDatabase() {
    abstract fun balooraDao(): BalooraDao

    companion object {
        @Volatile
        private var INSTANCE: BalooraDatabase? = null

        fun getDatabase(context: Context): BalooraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BalooraDatabase::class.java,
                    "baloora_social_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
