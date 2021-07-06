package com.example.myapplication.database

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Entity(tableName = "users")
    data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?
    )
    