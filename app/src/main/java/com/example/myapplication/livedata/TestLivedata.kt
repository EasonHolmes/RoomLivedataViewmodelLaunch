package com.example.myapplication.livedata

import androidx.lifecycle.LiveData

//https://www.cnblogs.com/guanxinjing/p/11544273.html
class TestLivedata : LiveData<TestLivedata>() {
    private var nameTest = ""

    fun setnameTest(name: String) {
        this.nameTest = name
//        此方法可以在其他线程中调用,如果同时调用 .postValue(“a”)和.setValue(“b”)，一定是值b被值a覆盖,如果在主线程执行发布的任务之前多次调用此方法，则仅将分配最后一个值。
        postValue(this)
//        此方法只能在主线程里调用
//        setvalue( this)
    }

    fun getNameTest() = nameTest
}