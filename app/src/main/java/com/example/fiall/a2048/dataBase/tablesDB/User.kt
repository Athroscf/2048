package com.example.fiall.a2048.dataBase.tablesDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
class User(@ColumnInfo(name = "user")
    var user: String = "", @ColumnInfo(name = "password")
    var password: String = "")
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}