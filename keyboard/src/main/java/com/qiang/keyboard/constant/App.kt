package com.qiang.keyboard.constant

import android.app.Application
import android.app.Service
import android.os.Vibrator
import com.daniel.edge.EdgeManager
import com.daniel.edge.management.application.EdgeApplicationManagement
import com.qiang.keyboard.R
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (EdgeApplicationManagement.isMainProcess(this)) {
            EdgeManager.initEdge(this)
                .initActivityManagement()
                .initDemoLog()
                .initDemoToolBar()
                .initToast()
            initOkHttp()
            initVibrator()
        }
    }

    fun initOkHttp() {
        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(NetworkConfig.DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(NetworkConfig.DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(NetworkConfig.DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS)
            .build()
    }

    private fun initVibrator() {
        vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
    }

    companion object {
        //初始化OkHttp
        lateinit var okHttpClient: OkHttpClient
        //按键的文字大小，因为TextView文字会自动转换，所以不需要通过resources获取
        var textSize = 16f
        //振动服务
        lateinit var vibrator: Vibrator
    }
}