package com.qiang.keyboard.constant

import android.app.Application
import com.daniel.edge.EdgeManager
import com.daniel.edge.management.application.EdgeApplicationManagement
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class App : Application() {
    companion object {
        lateinit var okHttpClient: OkHttpClient
    }

    override fun onCreate() {
        super.onCreate()
        if (EdgeApplicationManagement.isMainProcess(this)) {
            EdgeManager.initEdge(this)
                .initActivityManagement()
                .initDemoLog()
                .initDemoToolBar()
                .initToast()
            initOkHttp()
        }
    }

    fun initOkHttp() {
        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(NetworkConfig.DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(NetworkConfig.DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(NetworkConfig.DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS)
            .build()
    }
}