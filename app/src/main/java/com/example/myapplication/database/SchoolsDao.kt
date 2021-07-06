package com.example.myapplication.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface SchoolsDao {
    //Flow和suspend关键字不能同时使用
    @Query("SELECT * FROM schools")
    fun getAll(): Flow<List<Schools>>

    @Query("SELECT * FROM schools WHERE uid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<Schools>

    @Query(
        "SELECT * FROM schools WHERE school_name In (:names)"
    )
    //使用Flow返回值 数据库每次更新会触发每个@query进行更新
//    https://blog.csdn.net/jILRvRTrc/article/details/107147820
//    https://developer.android.google.cn/training/data-storage/room/accessing-data?hl=zh-cn
    fun findByName(names: String): Flow<Schools>

    //如果使用flow的方法不需要每次更新后通知，使用distinctUntilChanged()进行过滤；只要没有调用findByName的地方则编译生成的SchoolsDao_Impl下的findByName不会执行
    fun getSchoolDistinctUntilChanged(names:String) =
        findByName(names).distinctUntilChanged()

    @Query("SELECT * from schools limit 0,1")
    fun findfirstData():Flow<Schools>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg schools: Schools)

    @Delete
    suspend fun delete(user: User)


}
    