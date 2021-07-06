package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.livedata.TestLivedata

class ActViewModel : ViewModel() {
    // Create a LiveData with a String
    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val testliveData :TestLivedata = TestLivedata()

    fun getTestLivedata() = testliveData

//    fun getStrings(): LiveData<String> {
//        return users
//    }
//    fun setStrings(string: String){
//        users.value = string
//    }

    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.
    }

    override fun onCleared() {
        Log.e(ActViewModel::class.java.name,"onCleared()=====")
        super.onCleared()
    }
}