package com.daniel.edge.utils.system

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import com.daniel.edge.config.Edge

/**
 * 创建人 Daniel
 * 时间   2018/11/7.
 * 简介   xxx
 */
object EdgeSystemUtils {
    //判断当前应用是否是debug状态
    @JvmStatic
    fun isApkInDebug(): Boolean {
        try {
            val info = Edge.CONTEXT.applicationInfo
            return info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    @JvmStatic
    fun isMainProcess():Boolean{
        if (Edge.CONTEXT.getPackageName().equals(processName())){
            return true
        }else{
            return false
        }
    }

    @JvmStatic
    fun processName():String{
        var pid = android.os.Process.myPid()
        var manager = Edge.CONTEXT.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        manager.runningAppProcesses.forEach {
            if (it.pid == pid){
                return it.processName
            }
        }
        return ""
    }
}