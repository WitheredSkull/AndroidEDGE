package com.daniel.edge.utils.appCompat

import android.content.Context
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import android.view.View
import com.daniel.edge.config.Edge


/**
 * 创建人 Daniel
 * 时间   2018/11/6.
 * 简介   兼容多种系统API
 */
object EdgeAppCompat {
    //获取颜色值兼容6.0以下
    @JvmStatic
    fun getColor(@ColorRes res: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Edge.CONTEXT.getColor(res) else Edge.CONTEXT.resources.getColor(
            res
        )
    }

    //通过Layout获取View
    @JvmStatic
    fun getView(context: Context, @LayoutRes layoutRes: Int): View {
        return View.inflate(context, layoutRes, null)
    }
}