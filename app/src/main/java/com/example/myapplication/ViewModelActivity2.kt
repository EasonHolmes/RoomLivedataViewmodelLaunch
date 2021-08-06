package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.databinding.ActivityViewmodelBinding
import com.example.myapplication.viewmodel.ActViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


//livedata
//https://www.cnblogs.com/guanxinjing/p/11544273.html
class ViewModelActivity2 : AppCompatActivity() {
    private val binding: ActivityViewmodelBinding by lazy {
        ActivityViewmodelBinding.inflate(
            layoutInflater
        )
    }
    private val TAG by lazy { ViewModelActivity2::class.java.name }
    private val handler: Handler by lazy { Handler() }
    private val viewmode: ActViewModel by lazy { ViewModelProvider(this).get(ActViewModel::class.java) }
    private var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.textView.setOnClickListener {
            viewmode.currentName.value = "on changed"
            viewmode.currentName.observe(this){
                printLog("index111111===="+(++index).toString())
                binding.textView.text =it
            }
            viewmode.currentName.observe(this){
                printLog("index22222===="+(++index).toString())
                binding.textView.text = it
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun printLog(string: String) {
        Log.e(ViewModelActivity2::class.java.toString(), string)
    }


}