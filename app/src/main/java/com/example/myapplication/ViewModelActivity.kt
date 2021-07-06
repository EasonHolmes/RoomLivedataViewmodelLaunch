package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivityViewmodelBinding
import com.example.myapplication.viewmodel.ActViewModel


//livedata
//https://www.cnblogs.com/guanxinjing/p/11544273.html
class ViewModelActivity : AppCompatActivity() {
    private val binding: ActivityViewmodelBinding by lazy {
        ActivityViewmodelBinding.inflate(
            layoutInflater
        )
    }
    private val TAG by lazy { ViewModelActivity::class.java.name }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        两个方法相同，viewModels()只是activity的ktx，需要依赖activityktx库
//        val viewmode: ActViewModel by viewModels()
        val viewmode = ViewModelProvider(this).get(ActViewModel::class.java)

        viewmode.currentName.observe(this,{
            binding.textView.text = it
        })
        viewmode.getTestLivedata().observe(this, {
            Log.e(TAG, "getTestLivedata().observe===="+it.getNameTest())
        })

        binding.textView.setOnClickListener {
            val anotherName = "GitCode"
            viewmode.currentName.setValue(anotherName)
            viewmode.testliveData.setnameTest("kkkkkkk")
        }
    }
}