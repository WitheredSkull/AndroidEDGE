package com.daniel.edge.utils.system

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.provider.MediaStore
import com.daniel.edge.config.Edge

/**
 * 创建人 Daniel
 * 时间   2018/11/7.
 * 简介   xxx
 */
object EdgeSystemUtils {
    /**
     * @return 判断当前应用是否是debug状态
     */
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