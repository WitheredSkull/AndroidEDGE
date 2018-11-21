package com.daniel.edge

import android.app.Application
import com.shuanglu.edge.EdgeManager

/**
 * 创建人 Daniel
 * 时间   2018/11/6.
 * 简介   xxx
 */
class DemoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        EdgeManager.initEdge(this)
            .initDemoLog()//初始化日志功能
            .initDemoToolBar()//初始化工具栏
            .initActivityManagement()
    }
}