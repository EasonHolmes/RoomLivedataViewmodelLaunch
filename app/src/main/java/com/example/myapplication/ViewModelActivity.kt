package com.example.myapplication

import android.content.Intent
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
class ViewModelActivity : AppCompatActivity() {
    private val binding: ActivityViewmodelBinding by lazy {
        ActivityViewmodelBinding.inflate(
            layoutInflater
        )
    }
    private val TAG by lazy { ViewModelActivity::class.java.name }
    private val handler: Handler by lazy { Handler() }
    private val viewmode: ActViewModel by lazy { ViewModelProvider(this).get(ActViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        两个方法相同，viewModels()只是activity的ktx，需要依赖activityktx库
//        val viewmode: ActViewModel by viewModels()
        //注册绑定activity生命周期
        viewmode.currentName.observe(this, {
            binding.textView.text = it
        })
        viewmode.getTestLivedata().observe(this, {
            Log.e(TAG, "getTestLivedata().observe====" + it.getNameTest())
        })

        //当viewmode生命周期结束，协程内也会消毁，默认主线程可以加dispatchers
        viewmode.viewModelScope.launch(Dispatchers.IO) {

        }
        //shareFlow
//        whenResumed 可以有返回结果
//                launchWhenResumed 返回的是Job对象
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmode.testShareFlow.collect {
                    printLog("testShareFlow=====$it")
                }
            }
        }
        //stateFlow
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmode.observeState().collect {
                    printLog("testStateFlow===-----------==$it")
                }
            }
        }
        binding.textView.setOnClickListener {
            val anotherName = "GitCode"
            viewmode.currentName.value = anotherName
            startActivity(Intent(this, ViewModelActivity2::class.java))
            viewmode.testliveData.setnameTest("kkkkkkk")
            handler.postDelayed({
                viewmode.testShareFlow()
//                viewmode.testShareFlow("viewmode1111")
                viewmode.testShareFlow()
            }, 3000)
            handler.postDelayed({
                viewmode.testStateFlow()
                viewmode.testStateFlow()
                viewmode.testStateFlow()
//                viewmode.testStateFlow("viewmode22222")
//                viewmode.testStateFlow("234234")
            }, 3000)
            handler.postDelayed({ viewmode.testliveData.setnameTest("kkkkkkk") }, 3000)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun printLog(string: String) {
        Log.e(ViewModelActivity::class.java.toString(), string)
    }

//    open class TTFragment : Fragment() {
//        private val viewmode: ActViewModel by lazy { ViewModelProvider(this).get(ActViewModel::class.java) }
//
//        override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//        ): View? {
//            val button = Button(activity)
//            button.text = " click me "
//            testFlow()
//            return button
//        }
//
//        private fun testFlow() {
//            lifecycleScope.launch {
//                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                    viewmode.testShareFlow.collect { printLog("fragment=====testShareFlow==$it") }
//                }
//            }
//        }
//
//        fun printLog(string: String) {
//            Log.e(TTFragment::class.java.toString(), string)
//        }
//    }


}