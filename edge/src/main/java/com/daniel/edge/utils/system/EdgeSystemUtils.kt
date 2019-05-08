package com.daniel.edge.utils.system

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.MediaStore
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import androidx.core.content.PermissionChecker
import com.daniel.edge.config.Edge
import com.daniel.edge.utils.appCompat.EdgeAppCompat
import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.utils.netWork.EdgeNetWorkStatusUtils
import com.daniel.edge.utils.text.EdgeTextUtils
import java.io.File
import java.net.NetworkInterface
import java.util.*
import java.net.NetworkInterface.getNetworkInterfaces



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
     * 获取设备码
     */
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    @JvmStatic
    fun getIMEI(): String {
        val _t = Edge.CONTEXT.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var imei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _t.getImei()
        } else {
            _t.deviceId
        }
        return imei
    }

    /**
     * 获取Mac地址
     */
    @RequiresPermission(android.Manifest.permission.INTERNET)
    fun getMacAddress(): String {
//        val _w = Edge.CONTEXT.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//        if (!_w.isWifiEnabled) {
//            _w.setWifiEnabled(true)
//        }
//        val mac =_w.connectionInfo.macAddress
        var address = ""
        // 把当前机器上的访问网络接口的存入 Enumeration集合中
        val interfaces = getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val netWork = interfaces.nextElement()
            // 如果存在硬件地址并可以使用给定的当前权限访问，则返回该硬件地址（通常是 MAC）。
            val by = netWork.getHardwareAddress()
            if (by == null || by.size == 0) {
                continue
            }
            val builder = StringBuilder()
            for (b in by) {
                builder.append(String.format("%02X:", b))
            }
            if (builder.length > 0) {
                builder.deleteCharAt(builder.length - 1)
            }
            val mac = builder.toString()
//            Log.d("mac", "interfaceName=" + netWork.getName() + ", mac=" + mac)
            // 从路由器上在线设备的MAC地址列表，可以印证设备Wifi的 name 是 wlan0
            if (netWork.getName().equals("wlan0")) {
//                Log.d("mac", " interfaceName =" + netWork.getName() + ", mac=" + mac)
                address = mac
            }
        }
        EdgeLog.show(javaClass,"唯一码",address)
        return address
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
    fun getGalleryIntent(): Intent {
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        return Intent.createChooser(i, "Image Chooser")
    }

    /**
     * 新增图片，刷新系统图库
     */
    @JvmStatic
    fun systemGalleyInsert(path: String) {
        Edge.CONTEXT.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(path))))
    }
}