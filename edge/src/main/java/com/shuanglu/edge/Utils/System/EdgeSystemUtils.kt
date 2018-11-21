package com.shuanglu.edge.Utils.System

import android.content.pm.ApplicationInfo
import com.daniel.edge.Config.EdgeConfig

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
            val info = EdgeConfig.CONTEXT.applicationInfo
            return info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}