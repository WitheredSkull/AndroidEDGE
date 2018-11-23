package com.shuanglu.edge.Management.Application

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.util.ArrayMap
import com.daniel.edge.Config.EdgeConfig
import java.io.File

/**
 * 创建人 Daniel
 * 时间   2018/11/9.
 * 简介   xxx
 */
object EdgeApplicationManagement {
    //获取应用包名
    @JvmStatic
    fun appPackageName(): String {
        return EdgeConfig.CONTEXT.packageName
    }

    //获取包信息
    @JvmStatic
    fun appPackageInfo(packageName: String): PackageInfo? {
        var pm = EdgeConfig.CONTEXT.packageManager
        try {
            return pm.getPackageInfo(packageName, 0)
        }catch (e:PackageManager.NameNotFoundException){
            e.printStackTrace()
            return null
        }
    }

    //获取包所需要的权限
    @JvmStatic
    fun appPermissionFromPackageInfo(packageName: String): PackageInfo? {
        var pm = EdgeConfig.CONTEXT.packageManager
        try {
            return pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        }catch (e:PackageManager.NameNotFoundException){
            e.printStackTrace()
            return null
        }
    }
    //获取应用名
    @JvmStatic
    fun appName(packageName: String): String {
        return appPackageInfo(packageName)?.applicationInfo?.loadLabel(EdgeConfig.CONTEXT.packageManager).toString()
    }

    //获取应用版本信息
    @JvmStatic
    fun appVersionName(packageName: String): String? {
        return appPackageInfo(packageName)?.versionName
    }

    //获取应用版本号
    @JvmStatic
    fun appVersionCode(packageName: String): Long? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            appPackageInfo(packageName)?.longVersionCode
        } else {
            appPackageInfo(packageName)?.versionCode?.toLong()
        }
    }

    //获取应用图标
    @JvmStatic
    fun appIcon(packageName: String): Bitmap {
        var pm = EdgeConfig.CONTEXT.packageManager
        return (pm.getApplicationIcon(packageName) as BitmapDrawable).bitmap
    }

    //获取所有的应用，map key为user时为用户安装应用，key为system时为系统预装应用
    @JvmStatic
    fun allApplication(): HashMap<String, ArrayList<PackageInfo>>? {
        var pm = EdgeConfig.CONTEXT.packageManager
        var list = pm.getInstalledPackages(0)
        var map = HashMap<String, ArrayList<PackageInfo>>()
        var userList = arrayListOf<PackageInfo>()
        var systemList = arrayListOf<PackageInfo>()
        for (model in list) {
            if ((model.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) <= 0) {
                userList.add(model)
            } else {
                systemList.add(model)
            }
        }
        map.put("user", userList)
        map.put("system", systemList)
        return map
    }


    //卸载应用
    @JvmStatic
    fun unInstallApk(packageName: String) {
        var uri = Uri.parse("package:" + packageName)
        var intent = Intent(Intent.ACTION_DELETE, uri)
        EdgeConfig.CONTEXT.startActivity(intent)
    }

    //安装应用
    @JvmStatic
    fun InstallApk(path: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (Build.VERSION.SDK_INT >= 24) {
            var apkUri =
                FileProvider.getUriForFile(EdgeConfig.CONTEXT, EdgeApplicationManagement.appPackageName(), File(path))
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(File(path)), "application/vnd.android.package-archive");
        }
        EdgeConfig.CONTEXT.startActivity(intent);
    }
}