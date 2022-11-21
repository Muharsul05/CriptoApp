package com.magarusik.criptoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.magarusik.criptoapp.pojo.CoinPriceInfo

@Database(entities = [CoinPriceInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private var db: AppDatabase? = null
        private const val DB_Name = "main.db"
        private val LOCK = Any();

        fun getInstance(context: Context): AppDatabase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(context, AppDatabase::class.java, DB_Name).build()
                db = instance
                return instance
            }
        }
    }
    abstract fun coinPriceInfoDao(): CoinPriceInfoDao
}