package com.shuanglu.edge

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.daniel.edge.Config.EdgeConfig
import com.daniel.edge.Utils.Log.Model.EdgeLogConfig
import com.daniel.edge.Utils.Log.Model.EdgeLogType
import com.shuanglu.edge.Management.Activity.EdgeActivityManagement
import com.shuanglu.edge.Utils.AppCompat.EdgeAppCompat
import com.shuanglu.edge.View.ToolBar.TooBarViewUtils

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
        EdgeConfig.CONTEXT = application.applicationContext
        context = application.applicationContext
        this.application = application
    }

    //设置数据库版本哈
    fun initEdgeDatabaseVersion(version:Int):EdgeManager{
        EdgeConfig.DATABASE_VERSION = version
        return this
    }

    //初始化日志输出助手
    fun initDemoLog(): EdgeManager {
        EdgeLogConfig
            .build()
            .setType(EdgeLogType.WARN)
            .setLogName("EDGE")
            .setHeadText("佛祖保佑，永无BUG//  ")
            .setEndFlag(" END")
            .setLength(150)
            .setMarginLines(0)
            .setAutoReleaseCloseLog(true)
        return this
    }

    //初始化标题工具栏
    fun initDemoToolBar(): EdgeManager {
        TooBarViewUtils
            .getInstance()
            .setBackgroundColor(EdgeAppCompat.getColor(context, android.R.color.white))//设置背景
            .setTextColor(EdgeAppCompat.getColor(context, R.color.colorEdgePrimary))//设置文字颜色
            .setLineColor(EdgeAppCompat.getColor(context, R.color.colorEdgePrimaryDark))//设置底部线条颜色
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
}