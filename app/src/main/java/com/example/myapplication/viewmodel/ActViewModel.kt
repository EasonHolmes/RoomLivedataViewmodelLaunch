package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ObEntity
import com.example.myapplication.livedata.SingleLivedata
import com.example.myapplication.livedata.TestLivedata
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ActViewModel : ViewModel() {
    // Create a LiveData with a String
    val currentName: SingleLivedata<String> by lazy {
        SingleLivedata<String>()
    }
    val testliveData :TestLivedata = TestLivedata()

    fun getTestLivedata() = testliveData

    val testShareFlow: MutableSharedFlow<String> by lazy {
        MutableSharedFlow(1)
    }

    private val testStateFlow = MutableStateFlow(ObEntity())

    fun testShareFlow(string: String = "sdfsdfsdf"){
        viewModelScope.launch {
            testShareFlow.emit(string)
        }
    }
    fun observeState(): StateFlow<ObEntity> {
        return testStateFlow
    }
    fun testStateFlow(string: String = "testStateFlow=-=-=-=-=-"){
//        viewModelScope.launch {
//            testStateFlow.value = string
//        }
        testStateFlow.value = ObEntity(string)
    }

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