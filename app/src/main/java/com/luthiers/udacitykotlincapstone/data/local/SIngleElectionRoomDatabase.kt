package com.luthiers.udacitykotlincapstone.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.luthiers.udacitykotlincapstone.data.models.SingleElection

@Database(entities = [SingleElection::class], version = 1, exportSchema = false)
abstract class SingleElectionRoomDatabase: RoomDatabase() {
    abstract fun singleElectionDao(): SingleElectionDao

    companion object {
        @Volatile
        private var INSTANCE: SingleElectionRoomDatabase? = null

        fun getDatabase(context: Context): SingleElectionRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance == null) {
                synchronized(this) {
                    val instance = Room.databaseBuilder(context.applicationContext,
                    SingleElectionRoomDatabase::class.java,
                    "saved_elections_database")
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
            }

            return INSTANCE!!  // INSTANCE won't be null by now
        }
    }
}