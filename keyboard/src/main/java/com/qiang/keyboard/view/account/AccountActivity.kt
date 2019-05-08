package com.qiang.keyboard.view.account

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.daniel.edge.management.permission.EdgePermissionManagement
import com.daniel.edge.management.permission.OnEdgePermissionCallBack
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.qiang.keyboard.R
import com.qiang.keyboard.databinding.ActivityAccountBinding
import com.qiang.keyboard.view.base.BaseActivity
import com.qiang.keyboard.viewModel.AccountViewModel
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.toChannel
import kotlinx.coroutines.selects.select
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
import kotlin.math.max

class AccountActivity : BaseActivity<ActivityAccountBinding, AccountViewModel>() {

    override fun initDataBinding(): ActivityAccountBinding? {
        return DataBindingUtil.setContentView(this, R.layout.activity_account)
    }

    override fun initViewModel(dataBinding: ActivityAccountBinding) {
        setViewModel(AccountViewModel::class.java).apply {
            getDataBinding()?.viewModel = this
        }
        getViewModel()?.setFragmentManager(supportFragmentManager)

    }

    override fun initData() {
        getViewModel()?.switchFragment(AccountViewModel.AccountEnum.Login)

//        testCoroutineScope()

//        val channel = GlobalScope.produce {
//            repeat(1000){
//                send(it)
//            }
//        }
//
//
//        GlobalScope.launch(Dispatchers.Main) {
//            select<Unit> {
//                channel.onReceive{ value ->
//                    EdgeLog.show(javaClass, "仿Android异步", "1接收值${value}|线程${Thread.currentThread().name}")
//                }
//            }
//            for(item in channel){
//                tv_test.text = "${channel.isClosedForReceive}"
//                EdgeLog.show(javaClass, "仿Android异步", "2接收值${item}|线程${Thread.currentThread().name}")
//            }
//        }
//        GlobalScope.launch(Dispatchers.IO) {
//            repeat(500) {
//                delay(1000)
//                launch(Dispatchers.Main) {
//                    tv_test.text = "${channel.isClosedForReceive}"
//                }
//            }
//        }

//        GlobalScope.start<Int> { t ->
//            EdgeLog.show(javaClass, "仿Android异步", "开始")
//            var i = 0
//            repeat(10) {
//                t.send(it)
//                i = it
//            }
//            i
//        }.onReceiver {
//            EdgeLog.show(javaClass, "仿Android异步", "接收值${it}")
//        }.then {
//            EdgeLog.show(javaClass, "仿Android异步", "结束值${it}")
//        }
    }

    /**
     * 使用 coroutineScope
     * 构建器声明自己的作用域。
     * 它会创建一个协程作用域并且在所有已启动子协程执行完毕之前不会结束。
     * runBlocking 与 coroutineScope 的主要区别在于后者在等待所有子协程执行完毕时不会阻塞当前线程
     */
    fun testCoroutineScope() = runBlocking {
        EdgeLog.show(javaClass, "协程coroutineScope", "开始协程")

        coroutineScope {
            delay(1000)
            withTimeoutOrNull(1500) {
                repeat(1000) {
                    EdgeLog.show(javaClass, "循环", "次数|${it}")
                    delay(500)
                }
            }
            testLaunch()
            testRunBlocking()
            testThread()
        }
        EdgeLog.show(javaClass, "协程coroutineScope", "协程结束")
    }

    fun CoroutineScope.receive(channel: Channel<Int>) = launch {
        for (msg in channel) {
            EdgeLog.show(javaClass, "测试通道", "接收值|${msg}")
        }
    }

    /**
     * launch
     * 是否为阻塞协程：否
     * 是否可网络操作：是
     * 是否和主线程同步运行：否 类似异步运行
     * 是否可以操作UI：否
     */
    fun testLaunch() = GlobalScope.launch {
        GlobalScope.launch() {

        }
        network()
        delay(200)
        EdgeLog.show(javaClass, "协程launch", "线程名${Thread.currentThread().name}")
    }

    /**
     * runBlocking
     * 是否为阻塞协程：是
     * 是否和主线程同步运行：是
     * 是否可以操作UI：是
     */
    fun testRunBlocking() = runBlocking {
        delay(10)
        EdgeLog.show(javaClass, "协程runBlocking", "线程名${Thread.currentThread().name}")
        EdgeToastUtils.getInstance().show("runBlocking|线程名${Thread.currentThread().name}")
    }

    /**
     * thread
     * 协程Or线程：线程
     * 是否可网络操作：是
     * 是否阻塞：不阻塞线程
     * 是否和主线程同步运行：否
     * 是否可以操作UI：否
     */
    fun testThread() = thread {
        network()
    }

    fun network() {
        var connect = URL("https://wanandroid.com/wxarticle/chapters/json").openConnection() as HttpURLConnection
        connect.connect()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EdgePermissionManagement()
            .setCallBack(object : OnEdgePermissionCallBack {
                override fun onRequestPermissionSuccess() {
                }

                override fun onRequestPermissionFailure(permissions: ArrayList<String>) {
                }

            })
            .requestPermission()
            .build()
//        EdgeViewHelper.setOnClicks(this@AccountActivity, bt_login, bt_switch, tv_register)
    }

}
