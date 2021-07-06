package com.example.myapplication.database

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Entity(tableName = "schools")
    data class Schools(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "school_name") val name: String?,
    )
    