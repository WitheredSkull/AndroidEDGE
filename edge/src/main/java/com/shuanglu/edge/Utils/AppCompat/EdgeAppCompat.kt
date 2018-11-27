package com.shuanglu.edge.Utils.AppCompat

import android.content.Context
import android.os.Build
import android.support.annotation.ColorRes
import android.content.pm.ApplicationInfo
import com.daniel.edge.Config.EdgeConfig


/**
 * 创建人 Daniel
 * 时间   2018/11/6.
 * 简介   兼容多种系统API
 */
object EdgeAppCompat {
    //获取颜色值兼容6.0以下
    @JvmStatic
    fun getColor(@ColorRes res: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) EdgeConfig.CONTEXT.getColor(res) else EdgeConfig.CONTEXT.resources.getColor(
            res
        )
    }
}