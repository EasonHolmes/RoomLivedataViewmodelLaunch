package com.example.myapplication.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//https://www.jianshu.com/p/633ffc49da92 数据库升级
@Database(entities = [User::class, Schools::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun schoolDao(): SchoolsDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        //新建实体类表或列改实体类表字段结构，build后在build-generated-source-kapt下database有AppDatabase_Impl里面会自动生成语句
        //更新某表结构逻辑：创建新表，复制数据，删除原表，更改新表名为原表名
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //创建表
                database.execSQL(
                 "CREATE TABLE IF NOT EXISTS `user_new` (`uid` INTEGER NOT NULL, `first_name` TEXT, `last_name` TEXT, PRIMARY KEY(`uid`))"
//                    "CREATE TABLE user_new (first_name TEXT,last_name TEXT,uid INTEGER, PRIMARY KEY(uid))"
                )
                //复制表
                database.execSQL(
                    "INSERT INTO user_new (first_name, last_name,uid) SELECT first_name, last_name, uid FROM users"
                )
                //删除表
                database.execSQL("DROP TABLE users")
                //修改表名称
                database.execSQL("ALTER TABLE user_new RENAME TO users")
                //创建新的表，
                database.execSQL("CREATE TABLE IF NOT EXISTS `schools` (`uid` INTEGER NOT NULL, `school_name` TEXT, PRIMARY KEY(`uid`))")
            }
        }

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        /**
         * 创建数据库方法
         */
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "Sample.db"
            )
                .addMigrations(MIGRATION_1_2)//数据库更新
                .build()
    }


}
