package com.daniel.edge

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.daniel.edge.config.Edge
import com.daniel.edge.utils.log.model.EdgeLogConfig
import com.daniel.edge.utils.log.model.EdgeLogType
import com.daniel.edge.management.activity.EdgeActivityManagement
import com.daniel.edge.utils.appCompat.EdgeAppCompat
import com.daniel.edge.utils.toast.model.EdgeToastConfig
import com.daniel.edge.view.toolBar.TooBarViewUtils
import com.squareup.leakcanary.LeakCanary

/**
 * 创建人 Daniel
 * 时间   2018/11/6.
 * 简介   xxx
 */
class EdgeManager(application: Application) {
    private var application: Application
    private var context: Context

    companion object {
        @Volatile
        private var INSTANCE: EdgeManager? = null

        @Synchronized
        fun initEdge(application: Application): EdgeManager {
            if (INSTANCE == null) {
                synchronized(EdgeManager::class) {
                    if (INSTANCE == null) {
                        INSTANCE = EdgeManager(application)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    init {
        Edge.CONTEXT = application.applicationContext
        context = application.applicationContext
        this.application = application
        if (!LeakCanary.isInAnalyzerProcess(context)) {
            LeakCanary.install(application)
        }
    }

    //设置数据库版本哈
    fun initEdgeDatabaseVersion(version:Int):EdgeManager{
        Edge.DATABASE_VERSION = version
        return this
    }

    //初始化日志输出助手
    fun initDemoLog(): EdgeManager {
        EdgeLogConfig
            .build()
            .setType(EdgeLogType.WARN)
            .setLogName("EDGE")
            .setEndFlag(" END")
            .setLength(100)
            .setMarginLines(0)
            .setAutoReleaseCloseLog(true)
        return this
    }

    //初始化标题工具栏
    fun initDemoToolBar(): EdgeManager {
        TooBarViewUtils
            .getInstance()
            .setBackgroundColor(EdgeAppCompat.getColor(android.R.color.white))//设置背景
            .setTextColor(EdgeAppCompat.getColor(R.color.colorEdgePrimary))//设置文字颜色
            .setLineColor(EdgeAppCompat.getColor(R.color.colorEdgePrimaryDark))//设置底部线条颜色
            .setLineHeight(2)//设置线条高度
        return this
    }

    fun initActivityManagement(): EdgeManager {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
                EdgeActivityManagement.getInstance().remove(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                EdgeActivityManagement.getInstance().add(activity)
            }
        })
        return this
    }

    fun initToast():EdgeManager{
        EdgeToastConfig
            .getInstance()
            .setLayout(R.layout.layout_toast)//传入布局
            .setTextId(R.id.tv_message)//传入T布局需要设置文字的控件ID，必须是TextView
            .setBottomMargin(20)//设置距离底部边距
            .build()//构建
        return this
    }
}