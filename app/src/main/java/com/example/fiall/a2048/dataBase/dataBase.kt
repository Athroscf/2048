package com.example.fiall.a2048.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fiall.a2048.dataBase.tablesDB.Scores
import com.example.fiall.a2048.dataBase.tablesDB.User

@Database(entities = [User::class, Scores::class], version = 1, exportSchema = false)
abstract class BD2048: RoomDatabase() {
    abstract fun getGameDao(): GameDao

    // Singleton o solamente una instancia de esta clase
    companion object {
        val database = "BD2048"
        var bd: BD2048? = null

        fun getInstance(context: Context): BD2048? {
            if (bd == null) {
                bd = Room.databaseBuilder(
                    context,
                    BD2048::class.java,
                    database
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return bd
        }
    }
}