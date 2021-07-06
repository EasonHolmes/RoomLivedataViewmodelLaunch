package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import com.example.myapplication.database.Schools
import com.example.myapplication.database.User
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.viewmodel.ActViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


/**
 * https://www.kotlincn.net/docs/reference/coroutines/coroutine-context-and-dispatchers.html
 * suspend只是一个提醒关键字，并无实际作用，协程只能在其他协和方法内调用，协程只是一个自带线程调度的轻量线程。
 * 尽量使用lifecycle里的协程方法自动管理，不需要手动进行取消。需要新线程io类的使用withContext配Dispatchers调度线程
 * 非suspend方法调用需要使用runBlocking lifecycleScope.launchWhenResumed或自定义方法将协程方法传入来使用
 *
 * jetpack库里所有版本：https://developer.android.com/jetpack/androidx/versions/stable-channel?hl=zh-cn
 *
 */
class MainActivity : AppCompatActivity() {
    val userDao by lazy { Injection.provideUserDataSource(this) }
    val schoolsDao by lazy { Injection.provideSchoolDataSource(this) }
    val user = User(555333, "FirstName", "LastName")
    val schools = Schools(333555, "cuiyang")
    val TAG = this.javaClass.name
    lateinit var flowJion: Job
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textHello.setOnClickListener {
            val s =
                Schools(((Math.random() * 10000).toInt()), "这是随机名字" + System.currentTimeMillis())
            insertSchool(s)
            //        由于 schoolsDao.findfirstData()的返回值是FLow，当有更新数据库时，会自动触发查询的方法
            //更新数据库后就不需要专门写怎么更新，会自动调用所有@query的方法（自动调用的不是@dao里的findByName这种方法，而是编译时自动生成的java类里的方法）
        }
        binding.btnViewmodel.setOnClickListener { startActivity(Intent(this,ViewModelActivity::class.java)) }
        listenerInsertData()

        insertUser()

//        insertSchool(schools)

        runBlocking {
            flowJion = launch(Dispatchers.IO) {
                Log.e(TAG, Thread.currentThread().name + "launch")
            }
            Log.e(TAG, Thread.currentThread().name + "runBlocking")
        }

        lifecycleScope.launch {
            lifecycleScope.launchWhenCreated {
                schoolsDao.getSchoolDistinctUntilChanged("cuiyang").map {
                    Log.e(TAG, Thread.currentThread().name + "map")
                    Log.e(TAG, it.toString() + "map")
                }.flowOn(Dispatchers.IO)
                    .collect {
                        Log.e(TAG, Thread.currentThread().name + "collect")
                    }
                Log.e(TAG, Thread.currentThread().name + "lifecycleScope.launchWhenCreated ")
            }
            Log.e(TAG, Thread.currentThread().name + "lifecycleScope.launch ")
        }

        // 收集一个 Flow 的用法
        lifecycleScope.launch {
            whenResumed() {
                delay(10000)
                Log.e(TAG, Thread.currentThread().name + "delay whenResumed")
            }
            Log.e(TAG, Thread.currentThread().name + "lifecycleScope.launch22222")
        }

        //退出activity后job会自动回收
//        https://developer.android.com/topic/libraries/architecture/coroutines?hl=zh-cn
        lifecycleScope.launchWhenResumed {
            withContext(Dispatchers.IO) {
                Log.e(
                    TAG,
                    Thread.currentThread().name + "lifecycleScope.launchWhenResumed Dispatchers.IO111111"
                )
                delay(10000)
                Log.e(
                    TAG,
                    Thread.currentThread().name + "lifecycleScope.launchWhenResumed Dispatchers.IO"
                )
            }
            Log.e(TAG, Thread.currentThread().name + "lifecycleScope344444")
        }

        //退出activity后job会自动回收
        lifecycleScope.launchWhenCreated {
            Log.e(TAG, getUserByname().toString() + "tostringUser")
            getSchoolsName()
        }
    }

    private fun listenerInsertData() {
        //以下这两种写法都可以，但第二种看着更直观一些
//        lifecycleScope.launchWhenCreated {
//            schoolsDao.findfirstData()
//                .flowOn(Dispatchers.IO)
//                .flowOn(Dispatchers.Main)
//                .collect {
//                    Log.e(
//                        TAG,
//                        Thread.currentThread().name + it.toString() + "findLimitData55555555555"
//                    )
//                    if (binding.textHello.text == "点我改变") {
//                        binding.textHello.text = it.name
//                    } else {
//                        binding.textHello.text = "点我改变"
//                    }
//                }
//        }

//        lifecycleScope.launchWhenCreated是在主线程
//        withContext(Dispatchers.IO)在io线程交添加返回值
//      由于launchWhenCreated是非阻塞的阻塞线程，tt.collect的block会挂起直到withContext结束
//        由于 schoolsDao.findfirstData()的返回值是FLow，当有更新数据库时，会自动触发查询的方法
        lifecycleScope.launchWhenCreated {
            val tt = withContext(Dispatchers.IO) {
                Log.e(TAG, Thread.currentThread().name + "findLimitData3333333")
                schoolsDao.findfirstData()
            }
            tt.collect {
                Log.e(TAG, Thread.currentThread().name + it.toString() + "findLimitData55555555555")
                if (binding.textHello.text == "点我改变") {
                    binding.textHello.text = it.name
                } else {
                    binding.textHello.text = "点我改变"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        runBlocking {
            //非lifecycleScope需手动管理取消
            flowJion.cancelAndJoin()
            Log.e(TAG, Thread.currentThread().name + "onDestroy")
        }
    }

    private fun insertUser() = runBlocking {
        withContext(Dispatchers.IO) {
            userDao.insertAll(user)
            Log.e(TAG, Thread.currentThread().name + "withContext")
        }
        Log.e(TAG, Thread.currentThread().name + "runBlocking")
    }

    private fun insertSchool(schools: Schools) = lifecycleScope.launchWhenResumed {
        withContext(Dispatchers.IO) {
            schoolsDao.insertAll(schools)
            Log.e(TAG, Thread.currentThread().name + "insertSchool=====")
        }
    }


    private suspend fun getSchoolsName() {
        return withContext(Dispatchers.IO) {
            schoolsDao.getAll().collect { it ->
                it.forEach {
                    Log.e(TAG, it.toString() + "toStringSchools${it.uid}")
                }
            }
        }
    }

    private fun getSchoolByName() = lifecycleScope.launchWhenResumed {
        withContext(Dispatchers.IO) {

        }
    }

    private suspend fun getUserByname(): User {
        return withContext(Dispatchers.IO) {
            userDao.findByName("FirstName", "LastName")
        }
    }
}