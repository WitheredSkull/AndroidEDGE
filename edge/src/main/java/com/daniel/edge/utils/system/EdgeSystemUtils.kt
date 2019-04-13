package com.daniel.edge.utils.system

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.content.MimeTypeFilter
import com.daniel.edge.config.Edge
import com.daniel.edge.management.application.EdgeApplicationManagement
import com.daniel.edge.view.webView.model.EdgeWebChromeClient
import java.io.File

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

    /**
     * @sample isMainProcess 是否是主进程
     */
    @JvmStatic
    fun isMainProcess(): Boolean {
        if (Edge.CONTEXT.getPackageName().equals(processName())) {
            return true
        } else {
            return false
        }
    }

    /**
     * @sample processName 获取进程名
     */
    @JvmStatic
    fun processName(): String {
        var pid = android.os.Process.myPid()
        var manager = Edge.CONTEXT.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        manager.runningAppProcesses.forEach {
            if (it.pid == pid) {
                return it.processName
            }
        }
        return ""
    }

    /**
     * @param _u 输出路径
     * @return 获取相机Intent
     */
    @JvmStatic
    fun getCameraIntent(_u: Uri): Intent {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, _u)
        return cameraIntent
    }

    /**
     * @return 获取相册的Intent
     */
    @JvmStatic
    fun getGalleryIntent():Intent{
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        return Intent.createChooser(i, "Image Chooser")
    }
}