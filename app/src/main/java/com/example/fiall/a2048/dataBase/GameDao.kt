package com.example.fiall.a2048.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fiall.a2048.dataBase.tablesDB.User
import com.example.fiall.a2048.dataBase.tablesDB.Scores

@Dao
interface GameDao {

    @Query("SELECT COUNT(*) FROM User WHERE user = :user")
    fun getUsers(user: String): Int

    @Query("SELECT TOP(*) FROM Scores WHERE level = '3x3' || level = '4x4' || level = '6x6' || level = '8x8'")
    fun getScores(): List<Scores>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveScore(score: Scores)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveUser(user: User)
}