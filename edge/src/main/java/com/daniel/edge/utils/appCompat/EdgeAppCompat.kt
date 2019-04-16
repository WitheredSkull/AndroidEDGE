package com.daniel.edge.utils.appCompat

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import android.view.View
import androidx.core.content.FileProvider
import com.daniel.edge.config.Edge
import com.daniel.edge.management.application.EdgeApplicationManagement
import java.io.File


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

    /**
     * 振动,支持到8.0以上
     */
    @JvmStatic
    fun vibrator(vibrator: Vibrator, duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(duration)
        }
    }

    /**
     * 文件转Uri
     */
    @JvmStatic
    fun fileToUri(file: File): Uri {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(file)
        } else {
            return FileProvider.getUriForFile(
                Edge.CONTEXT,
                EdgeApplicationManagement.appPackageName() + ".EdgeFileProvider",
                file
            )
        }
    }

    /**
     * 路径转Uri
     */
    @JvmStatic
    fun pathToUri(path: String): Uri {
        return fileToUri(File(path))
    }
}