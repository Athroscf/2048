package com.example.fiall.a2048.dataBase.tablesDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Scores")
class Scores(@ColumnInfo(name = "user") var user: String = "",
             @ColumnInfo(name = "level") var level: String = "",
             @ColumnInfo(name = "points") var points: Int = 0,
             @ColumnInfo(name = "remainingMoves") var moves: Int = 0,
             @ColumnInfo(name = "time") var time: String = "")
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}