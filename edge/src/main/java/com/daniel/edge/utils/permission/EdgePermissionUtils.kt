package com.daniel.edge.utils.permission

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.daniel.edge.config.Edge
import com.daniel.edge.management.application.EdgeApplicationManagement

object EdgePermissionUtils {

    /**
     * @return 获取包名所需权限
     */
    fun getPackageNeedPermission(): ArrayList<String>? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions =
                EdgeApplicationManagement.appPackagePermissions(EdgeApplicationManagement.appPackageName())
            val listFilter = permissions?.filter {
                if (ContextCompat.checkSelfPermission(Edge.CONTEXT, it) == PackageManager.PERMISSION_GRANTED) {
                    false
                } else {
                    true
                }
            }
            val permissionList = arrayListOf<String>()
            permissionList.addAll(listFilter!!)
            return permissionList
        } else {
            return null
        }
    }

    /**
     * @return 过滤出来所有需要的权限
     */
    fun getNeedPermission(permission: Array<String>): List<String>? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Filtrate Agree Permission
            var list = permission.filter {
                ContextCompat.checkSelfPermission(
                    Edge.CONTEXT,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            }
            return list
        }
        return null
    }

    /**
     * @return 判断权限是否获取了
     */
    fun isAgree(permission: Array<String>): Boolean {
        val list = getNeedPermission(permission)
        if (list == null || list.size == 0) {
            return true
        } else {
            return false
        }
    }

}