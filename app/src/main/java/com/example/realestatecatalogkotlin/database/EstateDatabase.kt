package com.example.realestatecatalogkotlin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EstateDb::class], version = 1, exportSchema = false)
abstract class EstateDatabase : RoomDatabase() {
    abstract val estateDbDao: EstateDbDao

    companion object {
        @Volatile
        private var instance: EstateDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            EstateDatabase::class.java,
            "Estate_table"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}


